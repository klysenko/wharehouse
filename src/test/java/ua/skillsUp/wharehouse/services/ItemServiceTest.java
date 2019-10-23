package ua.skillsUp.wharehouse.services;

import com.sun.tools.javac.jvm.Items;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private OwnerContactRepository ownerContactRepository;
    @Mock
    private OwnerService ownerService;
    @Mock
    private ItemHistoryRepository itemHistoryRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void getAllItems() {
        ItemEntity item = new ItemEntity();
        item.setId(123L);
        item.setTitle("title");
        item.setPrice(BigDecimal.valueOf(123));

        Item expectedItem = new Item();
        expectedItem.setId(123L);
        expectedItem.setTitle("title");
        expectedItem.setPrice(BigDecimal.valueOf(123));

        List<ItemEntity> expectedItems = Collections.singletonList(item);

        given(itemRepository.findAll()).willReturn(expectedItems);

        //WHEN
        List<Item> actualItems = itemService.getAllItems();

        //THEN
        assertThat(actualItems).containsExactly(expectedItem);
    }

    @Test
    public void getItemsStatisticForLastDay() {
    }

    @Test
    public void getItemsStatisticForLastMonth() {
    }

    @Test
    public void getOwnerItems() {
        ItemEntity itemEntity1 = new ItemEntity();
        OwnerEntity ownerFirst = new OwnerEntity();
        ownerFirst.setId(1L);
        itemEntity1.setId(123L);
        itemEntity1.setTitle("title");
        itemEntity1.setPrice(BigDecimal.valueOf(123));
        itemEntity1.setOwner(ownerFirst);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setId(124L);
        itemEntity2.setTitle("title");
        itemEntity2.setPrice(BigDecimal.valueOf(123));
        itemEntity2.setOwner(ownerFirst);

        List<ItemEntity> items = new ArrayList<>();
        items.add(itemEntity1);
        items.add(itemEntity2);

        Owner owner = new Owner();
        owner.setId(1L);

        Item item1 = new Item();
        item1.setId(123L);
        item1.setTitle("title");
        item1.setPrice(BigDecimal.valueOf(123));
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(124L);
        item2.setTitle("title");
        item2.setPrice(BigDecimal.valueOf(123));
        item2.setOwner(owner);

        ownerFirst.setItems(items);

        given(itemRepository.findByOwnerId(1L)).willReturn(items);
        given(ownerRepository.findById(1L)).willReturn(java.util.Optional.of(ownerFirst));

        //WHEN
        List<Item> actualItems = itemService.getOwnerItems(1L);

        //THEN
        assertThat(actualItems).containsExactlyInAnyOrder(item1, item2);

    }

    @Test
    public void addOwnerItem() {
    }

    @Test
    public void withdrawItems() {
    }
}