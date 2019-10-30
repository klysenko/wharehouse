package ua.skillsUp.wharehouse.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.skillsUp.wharehouse.enums.ItemHistoryStatus;
import ua.skillsUp.wharehouse.exeptions.NoSuchOwnerException;
import ua.skillsUp.wharehouse.models.Category;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.ItemHistory;
import ua.skillsUp.wharehouse.models.ItemsStatistic;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.CategoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void should_Get_All_Items() {
        ItemEntity item = new ItemEntity();
        item.setId(123L);
        item.setTitle("title");
        item.setPrice(BigDecimal.valueOf(123));

        Item expectedItem = new Item();
        expectedItem.setId(123L);
        expectedItem.setTitle("title");
        expectedItem.setPrice(BigDecimal.valueOf(123));

        List<ItemEntity> expectedItems = singletonList(item);

        given(itemRepository.findAll()).willReturn(expectedItems);

        //WHEN
        List<Item> actualItems = itemService.getAllItems();

        //THEN
        assertThat(actualItems).containsExactly(expectedItem);
    }

    @Test
    public void should_get_owner_Items() {
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(1L);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle("title");
        List<ItemEntity> itemEntityList = singletonList(itemEntity);
        ownerEntity.setItems(itemEntityList);
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(ownerEntity));

        Item expectedItem = new Item();
        expectedItem.setTitle("title");

        //WHEN
        List<Item> actualItems = itemService.getOwnerItems(1L);

        //THEN
        assertThat(actualItems).containsExactlyInAnyOrder(expectedItem);

    }

    @Test
    public void should_throw_NoSuchOwnerException_if_no_owners_found_when_getting_owner_items() {
        // given
        long ownerId = 1;
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> itemService.getOwnerItems(ownerId))
                .isInstanceOf(NoSuchOwnerException.class)
                .hasMessage("No owner with ID '%s" + ownerId);

    }

    @Test
    public void should_add_owner_item() {
        // given
        String title = "item_title";
        BigDecimal price = new BigDecimal(1000);
        ItemHistory itemHistory = new ItemHistory();
        itemHistory.setCount(1);
        List<ItemHistory> itemHistories = singletonList(itemHistory);

        ItemHistoryEntity itemHistoryEntity = new ItemHistoryEntity();
        itemHistoryEntity.setId(itemHistory.getId());
        itemHistoryEntity.setDate(itemHistory.getDate());
        itemHistoryEntity.setCount(itemHistory.getCount());

        Category category = new Category();
        category.setDescription("category_description");
        category.setCategoryTitle("category_title");

        List<Category> categories = singletonList(category);

        Item item = new Item();
        item.setItemHistories(itemHistories);
        item.setPrice(price);
        item.setTitle(title);
        item.setCategories(categories);

        long ownerId = 1L;

        OwnerEntity ownerEntity = new OwnerEntity();

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(ownerEntity));

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setCategoryTitle(category.getCategoryTitle());
        categoryEntity.setDescription(category.getDescription());

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle(item.getTitle());
        itemEntity.setPrice(item.getPrice());
        itemEntity.setCategories(singletonList(categoryEntity));
        itemEntity.setOwner(ownerEntity);

        // when
        itemService.addOwnerItem(ownerId, item);

        // then
        ArgumentCaptor<ItemEntity> itemEntityArgumentCaptor = ArgumentCaptor.forClass(ItemEntity.class);
        ArgumentCaptor<List> itemHistoryEntityArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(itemRepository).save(itemEntityArgumentCaptor.capture());
        assertThat(itemEntityArgumentCaptor.getValue()).isEqualTo(itemEntity);
        verify(itemHistoryRepository).saveAll(itemHistoryEntityArgumentCaptor.capture());
        List<ItemHistoryEntity> actualItemHistoryEntities =
                (List<ItemHistoryEntity>) itemHistoryEntityArgumentCaptor.getValue();
        itemHistoryEntity.setItem(itemEntity);
        assertThat(actualItemHistoryEntities).containsExactly(itemHistoryEntity);
    }

    @Test
    public void should_throw_NoSuchOwnerException_if_no_owners_found_when_adding_owner_item() {
        // given
        long ownerId = 1;
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> itemService.addOwnerItem(ownerId, new Item()))
                .isInstanceOf(NoSuchOwnerException.class)
                .hasMessage("No owner with ID '%s" + ownerId);

    }

    @Test
    public void should_withdraw_items() {
        //GIVEN
        Map<Long, Integer> itemsByCount = new HashMap<>();
        long itemId = 1L;
        itemsByCount.put(itemId, 10);
        List<Long> itemIds = new ArrayList<>(itemsByCount.keySet());

        ItemHistoryEntity itemHistory = new ItemHistoryEntity();
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemId);
        itemHistory.setItem(itemEntity);
        itemHistory.setStatus(ItemHistoryStatus.STORED.toString());
        itemHistory.setCount(20);
        List<ItemHistoryEntity> itemHistoryEntityList = Collections.singletonList(itemHistory);
        when(itemHistoryRepository.findByItemIdsEagerly(itemIds)).thenReturn(itemHistoryEntityList);

        ItemHistoryEntity actualItemHistory = new ItemHistoryEntity();
        actualItemHistory.setCount(10);
        actualItemHistory.setStatus(ItemHistoryStatus.WITHDRAWED.toString());
        actualItemHistory.setItem(itemEntity);

        ArgumentCaptor<List> listArgumentCaptor =
                ArgumentCaptor.forClass(List.class);

        //WHEN
        itemService.withdrawItems(itemsByCount);

        //THEN
        verify(itemHistoryRepository).saveAll(listArgumentCaptor.capture());
        List<ItemHistoryEntity> actualItemHistoryEntities = listArgumentCaptor.getValue();
        assertThat(actualItemHistoryEntities).hasSize(1);
        ItemHistoryEntity actualItemHistoryEntity = actualItemHistoryEntities.get(0);

        Date actualDate = Date.from(actualItemHistoryEntity.getDate().atZone(ZoneId.systemDefault()).toInstant());
        assertThat(actualDate).isToday();
        assertThat(actualItemHistoryEntity.getItem()).isEqualTo(itemEntity);
        assertThat(actualItemHistoryEntity.getCount()).isEqualTo(10);
        assertThat(actualItemHistoryEntity.getStatus()).isEqualTo(ItemHistoryStatus.WITHDRAWED.toString());
    }

    @Test
    public void should_throw_IllegalArgumentException_when_balance_count_is_less_than_withdraw_count() {
        // given
        int count = 10;
        Map<Long, Integer> itemsByCount = new HashMap<>();
        long itemId = 1L;
        itemsByCount.put(itemId, count);
        List<Long> itemIds = new ArrayList<>(itemsByCount.keySet());

        ItemHistoryEntity itemHistory = new ItemHistoryEntity();
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemId);
        itemHistory.setItem(itemEntity);
        itemHistory.setStatus(ItemHistoryStatus.STORED.toString());
        itemHistory.setCount(count - 1);
        List<ItemHistoryEntity> itemHistoryEntityList = Collections.singletonList(itemHistory);
        when(itemHistoryRepository.findByItemIdsEagerly(itemIds)).thenReturn(itemHistoryEntityList);

        // when && then
        assertThatThrownBy(() -> itemService.withdrawItems(itemsByCount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can't withdraw %d items because only %d available", count, count - 1);
    }

    @Test
    public void should_return_items_statistic_for_last_day() {
        // given
        ItemHistoryEntity itemHistoryEntity1 = new ItemHistoryEntity();
        ItemEntity item1 = new ItemEntity();
        item1.setPrice(new BigDecimal(50));
        itemHistoryEntity1.setItem(item1);
        itemHistoryEntity1.setCount(2);

        ItemHistoryEntity itemHistoryEntity2 = new ItemHistoryEntity();
        ItemEntity item2 = new ItemEntity();
        item2.setPrice(new BigDecimal(100));
        itemHistoryEntity2.setItem(item1);
        itemHistoryEntity2.setCount(3);

        when(itemHistoryRepository.findByDateRange(any(), any()))
                .thenReturn(Arrays.asList(itemHistoryEntity1, itemHistoryEntity2));

        ItemsStatistic itemsStatistic = new ItemsStatistic(5, 0, new BigDecimal(250), new BigDecimal(0));

        // when
        ItemsStatistic actualItemsStatisticForLastDay = itemService.getItemsStatisticForLastDay();

        // then
        assertThat(actualItemsStatisticForLastDay).isEqualTo(itemsStatistic);
    }

    @Test
    public void should_return_items_statistic_for_last_month() {
        // given
        ItemHistoryEntity itemHistoryEntity1 = new ItemHistoryEntity();
        ItemEntity item1 = new ItemEntity();
        item1.setPrice(new BigDecimal(50));
        itemHistoryEntity1.setItem(item1);
        itemHistoryEntity1.setCount(2);

        ItemHistoryEntity itemHistoryEntity2 = new ItemHistoryEntity();
        ItemEntity item2 = new ItemEntity();
        item2.setPrice(new BigDecimal(100));
        itemHistoryEntity2.setItem(item1);
        itemHistoryEntity2.setCount(3);

        when(itemHistoryRepository.findByDateRange(any(), any()))
                .thenReturn(Arrays.asList(itemHistoryEntity1, itemHistoryEntity2));

        ItemsStatistic itemsStatistic = new ItemsStatistic(5, 0, new BigDecimal(250), new BigDecimal(0));

        // when
        ItemsStatistic actualItemsStatisticForLastDay = itemService.getItemsStatisticForLastMonth();

        // then
        assertThat(actualItemsStatisticForLastDay).isEqualTo(itemsStatistic);
    }
}