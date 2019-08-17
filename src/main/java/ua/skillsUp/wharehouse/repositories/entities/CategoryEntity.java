package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "TITLE")
    private String title;

    @Column (name = "DESCRIPTION")
    private String description;
}
