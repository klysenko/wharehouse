package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByOwnerId(Long ownerId);
}
