package ua.skillsUp.wharehouse.converters;

import ua.skillsUp.wharehouse.models.Category;
import ua.skillsUp.wharehouse.repositories.entities.CategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ItemCategoriesConverter {

    public static List<CategoryEntity> toCategoryEntity(List<Category> categories) {
        return categories.stream().map(category -> {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(category.getId());
            categoryEntity.setCategoryTitle(category.getCategoryTitle());
            categoryEntity.setDescription(category.getDescription());
            return categoryEntity;
        }).collect(Collectors.toList());

    }
}
