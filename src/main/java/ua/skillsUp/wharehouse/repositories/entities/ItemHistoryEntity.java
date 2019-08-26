package ua.skillsUp.wharehouse.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ITEM_HISTORY")
public class ItemHistoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private ItemEntity item;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "STATUS")
    private String status = "stored";
}
