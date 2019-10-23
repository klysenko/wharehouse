package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;

import java.time.LocalDateTime;
import java.util.List;


public interface ItemHistoryRepository extends JpaRepository<ItemHistoryEntity, Long> {

    @Query("SELECT i FROM ItemHistoryEntity i JOIN FETCH i.item WHERE i.item.id in (:itemIds)")
    List<ItemHistoryEntity> findByItemIdsEagerly(@Param("itemIds") List<Long> itemIds);

    @Query("SELECT i FROM ItemHistoryEntity i JOIN FETCH i.item WHERE i.status = :status")
    List<ItemHistoryEntity> findWithItemsByStatus(String status);

    @Query("SELECT i FROM ItemHistoryEntity i JOIN FETCH i.item WHERE i.date BETWEEN :startDate AND :endDate")
    List<ItemHistoryEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
