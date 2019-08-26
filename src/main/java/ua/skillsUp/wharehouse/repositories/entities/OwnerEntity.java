package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "OWNER")
public class OwnerEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<OwnerContactsEntity> contacts;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "COMPANY_NAME")
    private String companyName;
}
