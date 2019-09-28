package ua.skillsUp.wharehouse.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Item {
    private Long id;
    private String title;
    private BigDecimal price;
    private Owner owner;
    private List<ItemHistory> itemHistories;
    private List<Category> categories;
}
