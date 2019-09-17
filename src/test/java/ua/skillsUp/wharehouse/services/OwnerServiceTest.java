package ua.skillsUp.wharehouse.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.skillsUp.wharehouse.converters.OwnerConverter;
import ua.skillsUp.wharehouse.models.Owner;
import ua.skillsUp.wharehouse.repositories.OwnerRepository;
import ua.skillsUp.wharehouse.repositories.entities.OwnerEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OwnerServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @Mock
    OwnerConverter ownerConverter;

    @InjectMocks
    OwnerService ownerService;

    @Test
    public void testGetAllOwners() {
        OwnerEntity owner = new OwnerEntity();
        owner.setId(123L);
        owner.setFirstName("testFirstName");
        owner.setLastName("testLastName");
        owner.setCompanyName("testCompanyName");
        owner.setLogin("testLogin");

        Owner ownerConverted = new Owner();
        ownerConverted.setId(123L);
        ownerConverted.setFirstName("testFirstName");
        ownerConverted.setLastName("testLastName");
        ownerConverted.setCompanyName("testCompanyName");
        ownerConverted.setLogin("testLogin");

        List<OwnerEntity> expectedOwners = Arrays.asList(owner);
        when(ownerRepository.findAll()).thenReturn(expectedOwners);
       // doReturn(expectedOwners).when(ownerRepository).findAll();

        when(OwnerConverter.toOwner(owner)).thenReturn(ownerConverted);

        //WHEN
        List<Owner> actualOwners = ownerService.getAllOwners();

        //THEN
        assertThat(actualOwners).isEqualTo(expectedOwners);
    }

    @Test
    public void getAllActiveOwners() {
    }

    @Test
    public void deleteOwner() {
    }

    @Test
    public void storeOwner() {
    }

    @Test
    public void addOwnerContact() {
    }
}