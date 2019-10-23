package ua.skillsUp.wharehouse.converters;

import ua.skillsUp.wharehouse.models.ItemHistory;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ItemHistoryConverter {

    public static List<ItemHistoryEntity> toItemHistoryEntity(List<ItemHistory> itemHistories) {
        return itemHistories.stream().map(itemHistory -> {
            ItemHistoryEntity itemHistoryEntity = new ItemHistoryEntity();
            itemHistoryEntity.setId(itemHistory.getId());
            itemHistoryEntity.setDate(itemHistory.getDate());
            itemHistoryEntity.setCount(itemHistory.getCount());
            return itemHistoryEntity;
        }).collect(toList());
    }

    public static ItemHistory toItemHistory(ItemHistoryEntity entity) {
        ItemHistory itemHistory = new ItemHistory();
        itemHistory.setId(entity.getId());
        itemHistory.setDate(entity.getDate());
        itemHistory.setCount(entity.getCount());
        itemHistory.setStatus(entity.getStatus());

        return itemHistory;
    }


}
