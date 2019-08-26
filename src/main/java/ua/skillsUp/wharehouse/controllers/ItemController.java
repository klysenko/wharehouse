package ua.skillsUp.wharehouse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.services.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "wharehouse/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getAllItems() {
        log.info("There are owners");
        return itemService.getAllItems();
    }
}
