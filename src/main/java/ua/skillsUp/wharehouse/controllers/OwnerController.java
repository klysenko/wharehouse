package ua.skillsUp.wharehouse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.services.OwnerService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "wharehouse/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Owner> getAllOwners() {
        log.info("There are owners");
        return ownerService.getAll();
    }

    @GetMapping(path = "/{ownerId}/items",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getAllOwnerItems(@PathVariable(name = "ownerId") long ownerId) {
        log.info("There are items");
        return ownerService.getOwnerItems(ownerId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwner(@RequestBody @Valid Owner newOwner) {
        log.info("New owner added: {}", newOwner);
        ownerService.store(newOwner);
    }

    @PostMapping(path = "/{ownerId}/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwnerContact(@RequestBody OwnerContact ownerContact,
                                @PathVariable(name = "ownerId") long ownerId) {
        log.info("Owner id -> {}, contact -> {}", ownerId, ownerContact);
        ownerService.addOwnerContact(ownerId, ownerContact);
    }

    @PostMapping(path = "/{ownerId}/items",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addOwnerItem(@RequestBody Item item,
                                @PathVariable(name = "ownerId") long ownerId) {
        log.info("Owner id -> {}, item -> {}", ownerId, item);
        ownerService.addOwnerItem(ownerId, item);
    }
}
