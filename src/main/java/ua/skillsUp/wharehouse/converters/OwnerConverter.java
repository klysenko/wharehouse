package ua.skillsUp.wharehouse.converters;

import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class OwnerConverter {

    public static Owner toOwner(OwnerEntity entity) {
        if (entity == null) {
            return null;
        }
        Owner owner = new Owner();
        owner.setId(entity.getId());
        owner.setLogin(entity.getLogin());
        owner.setFirstName(entity.getFirstName());
        owner.setLastName(entity.getLastName());
        owner.setCompanyName(entity.getCompanyName());

        if (entity.getContacts() != null) {
            owner.setContactsList(entity.getContacts()
                    .stream()
                    .map(OwnerConverter::toOwnerContact)
                    .collect(toList()));
        }

        return owner;
    }

    public static List<Owner> toOwners(List<OwnerEntity> ownerEntities) {
        return ownerEntities.stream()
                .map(OwnerConverter::toOwner)
                .collect(toList());
    }

    private static OwnerContact toOwnerContact(OwnerContactsEntity entity) {
        OwnerContact ownerContact = new OwnerContact();
        ownerContact.setId(entity.getId());
        ownerContact.setContact(entity.getContact());
        ownerContact.setContactType(entity.getContactType());

        return ownerContact;
    }

    public static List<OwnerContactsEntity> toOwnerContactsEntity(List<OwnerContact> ownerContact) {
        return ownerContact.stream()
                .map(ownerContact1 -> {
            OwnerContactsEntity ownerContactsEntity = new OwnerContactsEntity();
            ownerContactsEntity.setId(ownerContact1.getId());
            ownerContactsEntity.setContact(ownerContact1.getContact());
            ownerContactsEntity.setContactType(ownerContact1.getContactType());
            return ownerContactsEntity;
        }).collect(Collectors.toList());
    }
}
