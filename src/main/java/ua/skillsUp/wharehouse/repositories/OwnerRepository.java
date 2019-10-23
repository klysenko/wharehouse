package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.List;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
    List<OwnerEntity> findAllByItemsNotNull();

    @Query("SELECT o FROM OwnerEntity o JOIN FETCH o.items")
    List<OwnerEntity> findOwnersWithItems();

}
