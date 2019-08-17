package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "ITEM")
public class ItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TITLE")
    private String title;

    @Column(name="PRICE")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    private OwnerEntity owner;

    @OneToMany(mappedBy = "item")
    private List<ItemHistoryEntity> itemHistory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_CATEGORY",
            joinColumns = {@JoinColumn(name = "ITEM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID")}
    )
    private Set<CategoryEntity> categories;

}
