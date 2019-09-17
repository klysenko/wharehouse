package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface ItemHistoryRepository extends JpaRepository<ItemHistoryEntity, Long> {
    List<ItemHistoryEntity> findByItemId(Long itemId);

    List<ItemHistoryEntity> findAllByDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);


    @Query("SELECT i FROM ItemHistoryEntity i JOIN FETCH i.item WHERE i.item.id in (:itemIds)")
    List<ItemHistoryEntity> findByItemIdsEagerly(@Param("itemIds") List<Long> itemIds);

}
