package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "TITLE")
    private String categoryTitle;

    @Column (name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<ItemEntity> items;
}
