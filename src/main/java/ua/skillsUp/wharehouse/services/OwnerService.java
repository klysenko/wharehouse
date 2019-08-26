package ua.skillsUp.wharehouse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsUp.wharehouse.converters.OwnerConverter;
import ua.skillsUp.wharehouse.exeptions.CanNotPerformOperationException;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static ua.skillsUp.wharehouse.converters.OwnerConverter.toOwnerContactsEntity;

@Slf4j
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerContactRepository ownerContactRepository;
    private final ItemRepository itemRepository;

    public OwnerService(OwnerRepository ownerRepository,
                        OwnerContactRepository ownerContactRepository,
                        ItemRepository itemRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerContactRepository = ownerContactRepository;
        this.itemRepository = itemRepository;
    }


    public List<Owner> getAllOwners() {
        List<OwnerEntity> entities = ownerRepository.findAll();

        return entities.stream()
                .map(OwnerConverter::toOwner)
                .collect(toList());
    }

    public List<Owner> getAllActiveOwners() {
        List<OwnerEntity> entities = ownerRepository.findAll();



        return entities.stream()
                .map(OwnerConverter::toOwner)
                .collect(toList());
    }

    public void deleteOwner(Long ownerId) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID {}" + ownerId);
        }
        List<ItemEntity> itemEntities = itemRepository.findByOwnerId(ownerId);
        if (!itemEntities.isEmpty()) {
            throw new CanNotPerformOperationException("Can not delete owner with ID {}" + ownerId);
        }
        ownerRepository.deleteById(ownerId);
    }

    @Transactional
    public void storeOwner(Owner owner) {
        OwnerEntity ownerEntity = new OwnerEntity();
        List<OwnerContactsEntity> ownerContactsEntities = toOwnerContactsEntity(owner.getContactsList());

        ownerEntity.setLogin(owner.getLogin());
        ownerEntity.setFirstName(owner.getFirstName());
        ownerEntity.setLastName(owner.getLastName());
        ownerEntity.setCompanyName(owner.getCompanyName());
        ownerRepository.save(ownerEntity);
        ownerContactsEntities.forEach(ownerContactsEntity -> ownerContactsEntity.setOwner(ownerEntity));
        ownerContactRepository.saveAll(ownerContactsEntities);
    }

    @Transactional
    public void addOwnerContact(long ownerId, OwnerContact ownerContact) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID {}" + ownerId);
        }
        OwnerContactsEntity entity = new OwnerContactsEntity();
        entity.setContact(ownerContact.getContact());
        entity.setContactType(ownerContact.getContactType());
        entity.setOwner(owner.get());
        ownerContactRepository.save(entity);
    }
}
