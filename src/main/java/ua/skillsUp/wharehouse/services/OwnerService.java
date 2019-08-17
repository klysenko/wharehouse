package ua.skillsUp.wharehouse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

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


    public List<Owner> getAll() {
        List<OwnerEntity> entities = ownerRepository.findAll();

        return entities.stream()
                .map(OwnerService::convertFromEntity)
                .collect(Collectors.toList());
    }

    public List<Item> getOwnerItems(Long ownerId) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID '%s" + ownerId);
        }
        List<ItemEntity> entities = itemRepository.findByOwner(ownerId);

        return entities.stream()
                .map(OwnerService::convertFromEntity)
                .collect(Collectors.toList());
    }

    private static Owner convertFromEntity(OwnerEntity entity) {
        Owner owner = new Owner();
        owner.setId(entity.getId());
        owner.setLogin(entity.getLogin());
        owner.setFirstName(entity.getFirstName());
        owner.setLastName(entity.getLastName());
        owner.setCompanyName(entity.getCompanyName());
        owner.setContactsList(entity.getContacts()
                .stream()
                .map(OwnerService::convertFromEntity)
                .collect(Collectors.toList()));

        return owner;
    }

    private static OwnerContact convertFromEntity(OwnerContactsEntity entity) {
        OwnerContact ownerContact = new OwnerContact();
        ownerContact.setId(entity.getId());
        ownerContact.setContact(entity.getContact());
        ownerContact.setContactType(entity.getContactType());

        return ownerContact;
    }

    private static Item convertFromEntity(ItemEntity entity) {
        Item item = new Item();
        item.setId(entity.getId());
        item.setTitle(entity.getTitle());
        item.setPrice(entity.getPrice());

        return item;
    }

    @Transactional
    public void store(Owner owner) {
        OwnerEntity ownerEntity = new OwnerEntity();
        OwnerContactsEntity contactEntity = new OwnerContactsEntity();
        contactEntity.setContact(owner.getContactsList().get(0).getContact());
        contactEntity.setContactType(owner.getContactsList().get(0).getContactType());

        ownerEntity.setLogin(owner.getLogin());
        ownerEntity.setFirstName(owner.getFirstName());
        ownerEntity.setLastName(owner.getLastName());
        ownerEntity.setCompanyName(owner.getCompanyName());
        ownerEntity.setContacts(singletonList(contactEntity));

        OwnerEntity savedOwner = ownerRepository.save(ownerEntity);
        contactEntity.setOwner(savedOwner);
        ownerContactRepository.save(contactEntity);
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


    @Transactional
    public void addOwnerItem(long ownerId, Item item) {
        Optional<OwnerEntity> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NoSuchOwnerException("No owner with ID '%s" + ownerId);
        }
        ItemEntity entity = new ItemEntity();
        ItemHistoryEntity itemHistory = new ItemHistoryEntity();
        itemHistory.setCount(item.getItemHistoryList().get(0).getCount());
        itemHistory.setDate(item.getItemHistoryList().get(0).getDate());
        itemHistory.setStatus(item.getItemHistoryList().get(0).getStatus());
        entity.setTitle(item.getTitle());
        entity.setPrice(item.getPrice());
        entity.setOwner(owner.get());
        entity.setItemHistory(singletonList(itemHistory));
        itemRepository.save(entity);
    }

}
