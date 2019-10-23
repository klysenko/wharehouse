package ua.skillsUp.wharehouse.converters;

import org.springframework.util.CollectionUtils;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;

import static java.util.stream.Collectors.toList;
import static ua.skillsUp.wharehouse.converters.OwnerConverter.toOwner;

public class ItemConverter {

    public static Item toItem(ItemEntity itemEntity) {
        Item item = new Item();
        item.setId(itemEntity.getId());
        item.setTitle(itemEntity.getTitle());
        item.setPrice(itemEntity.getPrice());
        item.setOwner(toOwner(itemEntity.getOwner()));
        if (!CollectionUtils.isEmpty(itemEntity.getItemHistory())) {
            item.setItemHistories(itemEntity.getItemHistory()
                    .stream()
                    .map(ItemHistoryConverter::toItemHistory)
                    .collect(toList()));
        }

        if (!CollectionUtils.isEmpty(itemEntity.getCategories())) {
            item.setCategories(itemEntity.getCategories().stream()
                    .map(ItemCategoriesConverter::toCategory)
                    .collect(toList()));
        }

        return item;
    }
}
