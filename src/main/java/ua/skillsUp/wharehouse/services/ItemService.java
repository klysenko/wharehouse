package ua.skillsUp.wharehouse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsUp.wharehouse.converters.ItemHistoryConverter;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Category;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.repositories.CategoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.CategoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static ua.skillsUp.wharehouse.converters.ItemCategoriesConverter.toCategoryEntity;
import static ua.skillsUp.wharehouse.converters.ItemHistoryConverter.toItemHistoryEntity;
import static ua.skillsUp.wharehouse.converters.OwnerConverter.toOwner;

@Slf4j
@Service
public class ItemService {

    private final OwnerRepository ownerRepository;
    private final ItemRepository itemRepository;
    private final ItemHistoryRepository itemHistoryRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(OwnerRepository ownerRepository,
                       ItemRepository itemRepository,
                       ItemHistoryRepository itemHistoryRepository,
                       CategoryRepository categoryRepository) {
        this.ownerRepository = ownerRepository;
        this.itemRepository = itemRepository;
        this.itemHistoryRepository = itemHistoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Item> getAllItems() {

        List<ItemEntity> entities = itemRepository.findAll();
        return entities.stream()
                .map(ItemService::toItem)
                .collect(toList());

    }

    public List<Item> getOwnerItems(Long ownerId) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID '%s" + ownerId);
        }

        return owner.get().getItems().stream()
                .map(itemEntity -> {
                    Item item = new Item();
                    item.setId(itemEntity.getId());
                    item.setTitle(itemEntity.getTitle());
                    item.setPrice(itemEntity.getPrice());
                    item.setOwner(toOwner(itemEntity.getOwner()));
                    item.setItemHistories(itemEntity.getItemHistory()
                            .stream()
                            .map(ItemHistoryConverter::toItemHistory)
                            .collect(toList()));
                    item.setCategories(itemEntity.getCategories().stream()
                            .map(ItemService::toCategory)
                            .collect(toList()));
                    return item;
                })
                .collect(toList());
    }


    private static Category toCategory(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setCategoryTitle(categoryEntity.getCategoryTitle());
        category.setDescription(categoryEntity.getDescription());
        return category;
    }

    private static Item toItem(ItemEntity entity) {
        Item item = new Item();
        item.setId(entity.getId());
        item.setTitle(entity.getTitle());
        item.setPrice(entity.getPrice());

        return item;
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
        itemRepository.save(itemEntity);
        itemHistoryEntities.forEach(itemHistoryEntity -> itemHistoryEntity.setItem(itemEntity));
        itemHistoryRepository.saveAll(itemHistoryEntities);
        itemEntity.setOwner(owner.get());
        itemEntity.setCategories(toCategoryEntity(item.getCategories()));

        owner.get().getItems().add(itemEntity);
    }

}
