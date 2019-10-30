package ua.skillsUp.wharehouse.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ua.skillsUp.wharehouse.converters.ItemHistoryConverter;
import ua.skillsUp.wharehouse.enums.ItemHistoryStatus;
import ua.skillsUp.wharehouse.models.Item;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.models.OwnerContact;
import ua.skillsUp.wharehouse.repositories.ItemHistoryRepository;
import ua.skillsUp.wharehouse.repositories.ItemRepository;
import ua.skillsUp.wharehouse.repositories.OwnerContactRepository;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.ItemEntity;
import ua.skillsUp.wharehouse.repositories.entities.ItemHistoryEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerContactsEntity;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private OwnerContactRepository ownerContactRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemHistoryRepository itemHistoryRepository;

    @InjectMocks
    private OwnerService ownerService;

    @Test
    public void should_Return_All_Owners() {
        OwnerEntity owner = new OwnerEntity();
        owner.setId(123L);
        owner.setFirstName("testFirstName");
        owner.setLastName("testLastName");
        owner.setCompanyName("testCompanyName");
        owner.setLogin("testLogin");

        Owner expectedOwner = new Owner();
        expectedOwner.setId(123L);
        expectedOwner.setFirstName("testFirstName");
        expectedOwner.setLastName("testLastName");
        expectedOwner.setCompanyName("testCompanyName");
        expectedOwner.setLogin("testLogin");
        expectedOwner.setContactsList(new ArrayList<>());
        expectedOwner.setItemList(new ArrayList<>());

        List<OwnerEntity> expectedOwners = singletonList(owner);
        given(ownerRepository.findAll()).willReturn(expectedOwners);

        //WHEN
        List<Owner> actualOwners = ownerService.getAllOwners();

        //THEN
        assertThat(actualOwners).containsExactly(expectedOwner);
    }

    @Test
    public void should_return_top_owners() {
        // given
        ItemEntity itemEntity1 = mockItemEntity(1, new BigDecimal(10), 21);
        ItemEntity itemEntity2 = mockItemEntity(2, new BigDecimal(100), 2);
        ItemEntity itemEntity3 = mockItemEntity(3, new BigDecimal(50), 1);
        ItemEntity itemEntity4 = mockItemEntity(4, new BigDecimal(10), 1);
        ItemEntity itemEntity5 = mockItemEntity(5, new BigDecimal(50), 20);
        ItemEntity itemEntity6 = mockItemEntity(6, new BigDecimal(500), 2);
        ItemEntity itemEntity7 = mockItemEntity(7, new BigDecimal(1), 5);

        List<ItemEntity> itemEntities = Arrays.asList(itemEntity1, itemEntity2, itemEntity3, itemEntity4,
                itemEntity5, itemEntity6, itemEntity7);

        given(itemRepository.findItemsWithHistory()).willReturn(itemEntities);

        // when
        List<Owner> topOwners = ownerService.getTopOwners();

        // then
        assertThat(topOwners).containsExactlyInAnyOrder(
                createOwnerWithId(6),
                createOwnerWithId(5),
                createOwnerWithId(1),
                createOwnerWithId(2),
                createOwnerWithId(3));
    }

    private Owner createOwnerWithId(long id) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setContactsList(new ArrayList<>());
        owner.setItemList(new ArrayList<>());
        return owner;
    }

    private ItemEntity mockItemEntity(long id, BigDecimal price, int count) {
        ItemEntity itemEntity = Mockito.mock(ItemEntity.class);
        when(itemEntity.getPrice()).thenReturn(price);
        ItemHistoryEntity itemHistoryEntity7 =
                new ItemHistoryEntity(id, itemEntity, LocalDateTime.now(), count, ItemHistoryStatus.STORED.toString());
        List<ItemHistoryEntity> itemHistoryEntityList7 = singletonList(itemHistoryEntity7);
        when(itemEntity.getItemHistory()).thenReturn(itemHistoryEntityList7);
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(id);
        when(itemEntity.getOwner()).thenReturn(ownerEntity);
        return itemEntity;
    }

    @Test
    public void getAllActiveOwners() {
        OwnerEntity owner1 = new OwnerEntity();
        owner1.setId(123L);
        owner1.setFirstName("testFirstName");
        owner1.setLastName("testLastName");
        owner1.setCompanyName("testCompanyName");
        owner1.setLogin("testLogin");
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setId(1L);
        itemEntity1.setPrice(new BigDecimal(500));
        ItemHistoryEntity itemHistoryEntity1 =
                new ItemHistoryEntity(1L, itemEntity1, LocalDateTime.now(), 2, ItemHistoryStatus.STORED.toString());
        List<ItemHistoryEntity> itemHistoryEntityList1 = singletonList(itemHistoryEntity1);
        itemEntity1.setItemHistory(itemHistoryEntityList1);
        owner1.setItems(singletonList(itemEntity1));

        OwnerEntity owner2 = new OwnerEntity();
        owner2.setId(124L);
        owner2.setFirstName("testFirstName");
        owner2.setLastName("testLastName");
        owner2.setCompanyName("testCompanyName");
        owner2.setLogin("testLogin");

        Item expectedItem = new Item();
        expectedItem.setId(1L);
        expectedItem.setPrice(new BigDecimal(500));
        expectedItem.setItemHistories(singletonList(ItemHistoryConverter.toItemHistory(itemHistoryEntity1)));

        Owner expectedOwner = new Owner();
        expectedOwner.setId(123L);
        expectedOwner.setFirstName("testFirstName");
        expectedOwner.setLastName("testLastName");
        expectedOwner.setCompanyName("testCompanyName");
        expectedOwner.setLogin("testLogin");
        expectedOwner.setContactsList(new ArrayList<>());
        expectedOwner.setItemList(singletonList(expectedItem));

        given(ownerRepository.findAllByItemsNotNull()).willReturn(singletonList(owner1));

        //WHEN
        List<Owner> actualOwners = ownerService.getAllActiveOwners();

        //THEN
        assertThat(actualOwners).containsExactly(expectedOwner);
    }

    @Test
    public void deleteOwner() {
        //GIVEN
        OwnerEntity ownerEntityToDelete = new OwnerEntity();
        ownerEntityToDelete.setId(1L);
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(ownerEntityToDelete));
        //WHEN
        ownerService.deleteOwner(1L);
        //THEN
        verify(ownerRepository).deleteById(1L);
    }

    @Test
    public void should_store_owner() {
        //GIVEN
        Owner owner = Mockito.mock(Owner.class);
        owner.setFirstName("testFirstName");
        when(owner.getLogin()).thenReturn("testLogin");
        when(owner.getFirstName()).thenReturn("testFirstName");
        when(owner.getLastName()).thenReturn("testLastName");
        when(owner.getCompanyName()).thenReturn("testCompanyName");
        OwnerContact contact = Mockito.mock(OwnerContact.class);

        when(contact.getContact()).thenReturn("contact");
        when(contact.getContactType()).thenReturn("contactType");
        when(contact.getId()).thenReturn(1L);
        List<OwnerContact> ownerContacts = singletonList(contact);
        when(owner.getContactsList()).thenReturn(ownerContacts);

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setFirstName("testFirstName");
        ownerEntity.setLastName("testLastName");
        ownerEntity.setCompanyName("testCompanyName");
        ownerEntity.setLogin("testLogin");
        OwnerContactsEntity contactEntity = new OwnerContactsEntity();
        List<OwnerContactsEntity> ownerContactsEntities = singletonList(contactEntity);
        contactEntity.setId(1L);
        contactEntity.setContact("contact");
        contactEntity.setContactType("contactType");
        contactEntity.setOwner(ownerEntity);

        //WHEN
        ownerService.storeOwner(owner);

        //THEN
        verify(ownerRepository).save(ownerEntity);
        verify(ownerContactRepository).saveAll(ownerContactsEntities);
    }
}