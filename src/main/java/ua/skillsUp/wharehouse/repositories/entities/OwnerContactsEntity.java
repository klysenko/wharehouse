package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "OWNER_CONTACTS")
public class OwnerContactsEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private OwnerEntity owner;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "CONTACT_TYPE")
    private String contactType;
}
