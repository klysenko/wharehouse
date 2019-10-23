package ua.skillsUp.wharehouse.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.skillsUp.wharehouse.enums.ItemHistoryStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = "item")
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
    private String status = ItemHistoryStatus.STORED.toString();
}
