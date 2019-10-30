package ua.skillsUp.wharehouse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsUp.wharehouse.converters.ItemConverter;
import ua.skillsUp.wharehouse.enums.ItemHistoryStatus;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.ItemsStatistic;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ua.skillsUp.wharehouse.converters.ItemCategoriesConverter.toCategoryEntity;
import static ua.skillsUp.wharehouse.converters.ItemHistoryConverter.toItemHistoryEntity;

@Slf4j
@Service
public class ItemService {
    private final OwnerRepository ownerRepository;
    private final ItemRepository itemRepository;
    private final ItemHistoryRepository itemHistoryRepository;

    public ItemService(OwnerRepository ownerRepository,
                       ItemRepository itemRepository,
                       ItemHistoryRepository itemHistoryRepository) {
        this.ownerRepository = ownerRepository;
        this.itemRepository = itemRepository;
        this.itemHistoryRepository = itemHistoryRepository;
    }

    public List<Item> getAllItems() {
        List<ItemEntity> entities = itemRepository.findAll();
        return entities.stream()
                .map(ItemConverter::toItem)
                .collect(toList());

    }

    public ItemsStatistic getItemsStatisticForLastDay() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return getItemsStatisticForPeriod(startDate, endDate);
    }

    public ItemsStatistic getItemsStatisticForLastMonth() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusMonths(1L), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return getItemsStatisticForPeriod(startDate, endDate);
    }

    private ItemsStatistic getItemsStatisticForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<ItemHistoryEntity> itemsWithHistories = itemHistoryRepository.findByDateRange(startDate, endDate);

        List<ItemHistoryEntity> storedItemHistories = getItemsHistoryWithStatus(itemsWithHistories, ItemHistoryStatus.STORED);

        Integer storedAmount = getTotalAmount(storedItemHistories);

        BigDecimal storedTotalCost = getTotalCost(storedItemHistories);

        List<ItemHistoryEntity> withdrawnItemHistories = getItemsHistoryWithStatus(itemsWithHistories, ItemHistoryStatus.WITHDRAWED);

        Integer withdrawnAmount = getTotalAmount(withdrawnItemHistories);

        BigDecimal withdrawnTotalCost = getTotalCost(withdrawnItemHistories);

        return new ItemsStatistic(storedAmount, withdrawnAmount, storedTotalCost, withdrawnTotalCost);
    }

    private Integer getTotalAmount(List<ItemHistoryEntity> itemHistoryEntities) {
        return itemHistoryEntities.stream()
                .map(ItemHistoryEntity::getCount)
                .reduce(0, Integer::sum);
    }

    private BigDecimal getTotalCost(List<ItemHistoryEntity> itemHistoryEntities) {
        return itemHistoryEntities.stream()
                .map(itemHistoryEntity -> itemHistoryEntity.getItem().getPrice().multiply(BigDecimal.valueOf(itemHistoryEntity.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ItemHistoryEntity> getItemsHistoryWithStatus(List<ItemHistoryEntity> itemHistoryEntities, ItemHistoryStatus status) {
        return itemHistoryEntities.stream()
                .filter(itemHistoryEntity -> itemHistoryEntity.getStatus().equals(status.toString()))
                .collect(Collectors.toList());
    }

    public List<Item> getOwnerItems(Long ownerId) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID '%s" + ownerId);
        }

        return owner.get().getItems().stream()
                .map(ItemConverter::toItem)
                .collect(toList());
    }

    @Transactional
    public void addOwnerItem(long ownerId, Item item) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID '%s" + ownerId);
        }
        List<ItemHistoryEntity> itemHistoryEntities = toItemHistoryEntity(item.getItemHistories());

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle(item.getTitle());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setCategories(toCategoryEntity(item.getCategories()));

        itemRepository.save(itemEntity);

        itemHistoryEntities.forEach(itemHistoryEntity -> itemHistoryEntity.setItem(itemEntity));
        itemHistoryRepository.saveAll(itemHistoryEntities);
        itemEntity.setOwner(owner.get());
        owner.get().getItems().add(itemEntity);
    }

    @Transactional
    public void withdrawItems(Map<Long, Integer> itemIdsByCount) {
        List<Long> itemIds = new ArrayList<>(itemIdsByCount.keySet());
        Map<Long, List<ItemHistoryEntity>> itemIdByItemHistories =
                itemHistoryRepository.findByItemIdsEagerly(itemIds).stream()
                        .collect(Collectors.groupingBy(i -> i.getItem().getId()));

        List<ItemHistoryEntity> itemHistoryEntityToSave = itemIdsByCount.entrySet().stream()
                .map(itemIdsByCountEntry -> {
                    Long itemId = itemIdsByCountEntry.getKey();
                    Integer count = itemIdsByCountEntry.getValue();
                    List<ItemHistoryEntity> itemHistories = itemIdByItemHistories.get(itemId);
                    int storedCount = sumCount(itemHistories, ItemHistoryStatus.STORED);
                    int withdrawedCount = sumCount(itemHistories, ItemHistoryStatus.WITHDRAWED);

                    int balanceCount = storedCount - withdrawedCount;

                    if (balanceCount < count) {
                        throw new IllegalArgumentException(
                                String.format("Can't withdraw %d items because only %d available", count, balanceCount)
                        );
                    }

                    ItemHistoryEntity itemHistoryEntity = new ItemHistoryEntity();
                    itemHistoryEntity.setStatus(ItemHistoryStatus.WITHDRAWED.toString());
                    itemHistoryEntity.setDate(LocalDateTime.now());
                    itemHistoryEntity.setCount(count);
                    itemHistoryEntity.setItem(itemHistories.get(0).getItem());
                    return itemHistoryEntity;
                })
                .collect(toList());

        itemHistoryRepository.saveAll(itemHistoryEntityToSave);
    }

    private int sumCount(List<ItemHistoryEntity> itemHistories, ItemHistoryStatus status) {
        return itemHistories.stream()
                .filter(itemHistory -> itemHistory.getStatus().equals(status.toString()))
                .map(ItemHistoryEntity::getCount)
                .reduce(0, Integer::sum);
    }
}
