package ua.skillsUp.wharehouse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.services.ItemService;
import ua.skillsUp.wharehouse.services.OwnerService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "wharehouse/owners")
public class OwnerController {
    private final OwnerService ownerService;
    private final ItemService itemService;

    public OwnerController(OwnerService ownerService,
                           ItemService itemService) {
        this.ownerService = ownerService;
        this.itemService = itemService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Owner> getAllOwners() {
        log.info("Getting all owners");
        return ownerService.getAllOwners();
    }

    @GetMapping(path = "/active",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Owner> getAllActiveOwners() {
        log.info("Getting all active owners");
        return ownerService.getAllActiveOwners();
    }

    @GetMapping(path = "/{ownerId}/items",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getAllOwnerItems(@PathVariable(name = "ownerId") long ownerId) {
        log.info("Getting all owner items");
        return itemService.getOwnerItems(ownerId);
    }

    @GetMapping(path = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Owner> getTopOwners() {
        log.info("Getting top owners");
        return ownerService.getTopOwners();
    }

    @DeleteMapping(path = "/delete/{ownerId}")
    public void deleteOwner(@PathVariable(name = "ownerId") long ownerId) {
        log.info("Delete owner with id: {}", ownerId);
        ownerService.deleteOwner(ownerId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwner(@RequestBody @Valid Owner newOwner) {
        log.info("Add new owner: {}", newOwner);
        ownerService.storeOwner(newOwner);
    }

    @PostMapping(path = "/{ownerId}/contacts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwnerContact(@RequestBody OwnerContact ownerContact,
                                @PathVariable(name = "ownerId") long ownerId) {
        log.info("Adding owner contact: {} for owner with id: {}", ownerContact, ownerId);
        ownerService.addOwnerContact(ownerId, ownerContact);
    }

    @PostMapping(path = "/{ownerId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwnerItem(@RequestBody Item item, @PathVariable(name = "ownerId") long ownerId) {
        log.info("Adding owner item: {} for owner with id: {}", item, ownerId);
        itemService.addOwnerItem(ownerId, item);
    }
}
