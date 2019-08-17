package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
}
