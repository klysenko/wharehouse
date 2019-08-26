package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;


public interface ItemHistoryRepository extends JpaRepository<ItemHistoryEntity, Long> {
}
