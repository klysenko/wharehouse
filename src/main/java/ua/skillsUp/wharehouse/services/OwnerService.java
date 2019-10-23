package ua.skillsUp.wharehouse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsUp.wharehouse.converters.OwnerConverter;
import ua.skillsUp.wharehouse.enums.ItemHistoryStatus;
import ua.skillsUp.wharehouse.exeptions.CanNotPerformOperationException;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static ua.skillsUp.wharehouse.converters.OwnerConverter.toOwnerContactsEntity;
import static ua.skillsUp.wharehouse.converters.OwnerConverter.toOwners;

@Slf4j
@Service
public class OwnerService {
    private static final long TOP_OWNERS_LIMIT = 5L;

    private final OwnerRepository ownerRepository;
    private final OwnerContactRepository ownerContactRepository;
    private final ItemRepository itemRepository;
    private final ItemHistoryRepository itemHistoryRepository;

    public OwnerService(OwnerRepository ownerRepository,
                        OwnerContactRepository ownerContactRepository,
                        ItemRepository itemRepository,
                        ItemHistoryRepository itemHistoryRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerContactRepository = ownerContactRepository;
        this.itemRepository = itemRepository;
        this.itemHistoryRepository = itemHistoryRepository;
    }

    public List<Owner> getAllOwners() {
        List<OwnerEntity> entities = ownerRepository.findAll();

        return entities.stream()
                .map(OwnerConverter::toOwner)
                .collect(toList());
    }

    public List<Owner> getAllActiveOwners() {
        List<OwnerEntity> entities = ownerRepository.findAllByItemsNotNull();

        return entities.stream()
                .map(OwnerConverter::toOwner)
                .collect(toList());
    }

    public void deleteOwner(Long ownerId) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID {}" + ownerId);
        }
        List<ItemEntity> itemEntities = itemRepository.findByOwnerId(ownerId);
        if (!itemEntities.isEmpty()) {
            throw new CanNotPerformOperationException("Can not delete owner with ID {}" + ownerId);
        }
        ownerRepository.deleteById(ownerId);
    }

    @Transactional
    public void storeOwner(Owner owner) {
        OwnerEntity ownerEntity = new OwnerEntity();
        List<OwnerContactsEntity> ownerContactsEntities = toOwnerContactsEntity(owner.getContactsList());

        ownerEntity.setLogin(owner.getLogin());
        ownerEntity.setFirstName(owner.getFirstName());
        ownerEntity.setLastName(owner.getLastName());
        ownerEntity.setCompanyName(owner.getCompanyName());
        ownerRepository.save(ownerEntity);
        ownerContactsEntities.forEach(ownerContactsEntity -> ownerContactsEntity.setOwner(ownerEntity));
        ownerContactRepository.saveAll(ownerContactsEntities);
    }

    @Transactional
    public void addOwnerContact(long ownerId, OwnerContact ownerContact) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner found with ID: {}" + ownerId);
        }
        OwnerContactsEntity entity = new OwnerContactsEntity();
        entity.setContact(ownerContact.getContact());
        entity.setContactType(ownerContact.getContactType());
        entity.setOwner(owner.get());
        ownerContactRepository.save(entity);
    }

    @Transactional
    public List<Owner> getTopOwners() {
        List<ItemEntity> items = itemRepository.findItemsWithHistory();

        Map<ItemEntity, BigDecimal> totalCostByItem = new HashMap<>();

        items.forEach(itemEntity -> {
            BigDecimal totalPrice =
                    itemEntity.getPrice().multiply(new BigDecimal(calcTotalCount(itemEntity.getItemHistory())));
            totalCostByItem.put(itemEntity, totalPrice);
        });

        List<OwnerEntity> ownersWithItems = totalCostByItem.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(TOP_OWNERS_LIMIT)
                .map(entry -> entry.getKey().getOwner())
                .collect(toList());

        return toOwners(ownersWithItems);
    }

    private int calcTotalCount(List<ItemHistoryEntity> itemHistory) {
        return itemHistory.stream()
                .map(itemHistoryEntity -> {
                    int itemHistoryCount = itemHistoryEntity.getCount();
                    if (itemHistoryEntity.getStatus().equals(ItemHistoryStatus.STORED.toString())) {
                        return itemHistoryCount;
                    }
                    return itemHistoryCount * -1;
                })
                .reduce(0, Integer::sum);
    }


}
