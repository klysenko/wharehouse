package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM ItemEntity i JOIN FETCH i.itemHistory")
    List<ItemEntity> findItemsWithHistory();
}
