package ua.skillsUp.wharehouse.models;

import lombok.Data;
import ua.skillsUp.wharehouse.repositories.entities.CategoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class Item {

    private Long id;
    private String title;
    private BigDecimal price;
    private OwnerEntity ownerEntityId;
    private List<ItemHistory> itemHistoryList;
    private Set<CategoryEntity> categories;

}
