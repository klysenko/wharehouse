package ua.skillsUp.wharehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;

public interface OwnerContactRepository extends JpaRepository<OwnerContactsEntity, Long> {
}
