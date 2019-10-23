package ua.skillsUp.wharehouse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.ItemsStatistic;
import ua.skillsUp.wharehouse.services.ItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info("Getting all items");
        return itemService.getAllItems();
    }

    @GetMapping(path = "/statisticForLastMonth")
    public ItemsStatistic getItemsStatisticForLastMonth() {
        log.info("Getting statistic for last month");
        return itemService.getItemsStatisticForLastMonth();
    }

    @GetMapping(path = "/statisticForLastDay")
    public ItemsStatistic getItemsStatisticForLastDay() {
        log.info("Getting statistic for last day");
        return itemService.getItemsStatisticForLastDay();
    }

    @PostMapping(path = "/{itemId}/{count}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void withdrawItem(
            @PathVariable(name = "itemId") long itemId,
            @PathVariable(name = "count") int count) {
        log.info("Withdraw {} item with id: {}", count, itemId);
        Map<Long, Integer> itemIdByCount = new HashMap<>();
        itemIdByCount.put(itemId, count);
        itemService.withdrawItems(itemIdByCount);
    }

    @PostMapping(path = "withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void withdrawItems(@RequestBody HashMap<Long, Integer> itemsWithCount) {
        log.info("Withdraw items. Items ids by count : {}", itemsWithCount);
        itemService.withdrawItems(itemsWithCount);
    }
}
