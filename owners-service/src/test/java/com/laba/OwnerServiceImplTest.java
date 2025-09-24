package com.laba;

import com.laba.dao.OwnerDao;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.impl.OwnerServiceImpl;
import com.laba.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

    @Mock
    private OwnerDao ownerDao;
    private Validation validation;
    private OwnerServiceImpl ownerService;

    @BeforeEach
    void setUp() {
        validation = new Validation();
        validation.getOwner().setName(50);
        validation.getCat().setName(50);
        validation.getCat().setBreed(50);
        ownerService = new OwnerServiceImpl(ownerDao, validation);
    }
    @Test
    void getOwnerById_success() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("O");
        when(ownerDao.findByIdWithCats(1L)).thenReturn(Optional.of(owner));

        OwnerDto result = ownerService.getOwnerById(1L);

        assertEquals(1L, result.getId());
        assertEquals("O", result.getName());
    }

    @Test
    void getOwnerById_throwException_whenOwnerNotFound() {
        when(ownerDao.findByIdWithCats(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ownerService.getOwnerById(1L));
    }

    @Test
    void getAllOwners_returnsList() {
        Owner owner1 = new Owner();
        owner1.setId(1L);
        Owner owner2 = new Owner();
        owner2.setId(2L);
        when(ownerDao.findAllWithCats()).thenReturn(List.of(owner1, owner2));
        List<OwnerDto> result = ownerService.getAllOwners();
        assertEquals(2, result.size());
    }

    @Test
    void findOwnersByFilter_returnsFilteredOwners() {
        OwnerDto filter = new OwnerDto();
        filter.setName("O");
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("O");
        when(ownerDao.findAll(any(Specification.class))).thenReturn(List.of(owner));

        List<OwnerDto> result = ownerService.findOwnersByFilter(filter);
        assertEquals(1, result.size());
        assertEquals("O", result.get(0).getName());
    }

    @Test
    void findOwnersByName_returnsList() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("O");
        when(ownerDao.findByNameIgnoreCase("O")).thenReturn(List.of(owner));

        List<OwnerDto> result = ownerService.findOwnersByName("O");
        assertEquals(1, result.size());
        assertEquals("O", result.get(0).getName());
    }
    @Test
    void findOwnerByCatId_returnsOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        when(ownerDao.findByCats_Id(1L)).thenReturn(Optional.of(owner));
        OwnerDto result = ownerService.findOwnerByCatId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void findOwnerByCatId_throwsException_catIdNull() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerByCatId(null));
    }

    @Test
    void findOwnerByCatId_throwsException_ownerNotFound() {
        when(ownerDao.findByCats_Id(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerByCatId(1L));
    }

    @Test
    void saveOwner_success() {
        OwnerDto dto = new OwnerDto();
        dto.setName("O");
        dto.setBirthday(LocalDate.of(2020, 1, 1));
        Owner saved = new Owner();
        saved.setId(1L);
        saved.setName("O");
        saved.setBirthday(LocalDate.of(2020, 01,01));
        when(ownerDao.save(any(Owner.class))).thenReturn(saved);

        OwnerDto result = ownerService.saveOwner(dto);
        assertEquals(1L, result.getId());
        assertEquals("O", result.getName());
    }

    @Test
    void saveOwner_throwsException_dtoNull() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(null));
    }

    @Test
    void saveOwner_throwsException_birthdayInFuture() {
        OwnerDto dto = new OwnerDto();
        dto.setName("O");
        dto.setBirthday(LocalDate.now().plusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(dto));
        assertEquals("Invalid birthday", exception.getMessage());
        verify(ownerDao, never()).save(any());
    }

    @Test
    void updateOwner_success() {
        OwnerDto dto = new OwnerDto();
        dto.setId(1L);
        dto.setName("O");
        dto.setBirthday(LocalDate.of(2020, 1, 1));
        Owner updated = new Owner();
        updated.setId(1L);
        updated.setName("O");
        updated.setBirthday(LocalDate.of(2020, 1, 1));

        when(ownerDao.save(any(Owner.class))).thenReturn(updated);
        OwnerDto result = ownerService.updateOwner(dto);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateOwner_throwsException_dtoNull() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateOwner(null));
    }

    @Test
    void deleteOwner_success() {
        Owner owner = new Owner(); owner.setId(1L); owner.setCats(List.of());
        when(ownerDao.findByIdWithCats(1L)).thenReturn(Optional.of(owner));
        ownerService.deleteOwner(1L);
        verify(ownerDao, times(1)).delete(owner);
    }

    @Test
    void deleteOwner_throwsException_withCats() {
        Owner owner = new Owner(); owner.setId(1L); owner.setCats(List.of(new Cat()));
        when(ownerDao.findByIdWithCats(1L)).thenReturn(Optional.of(owner));

        assertThrows(IllegalStateException.class, () -> ownerService.deleteOwner(1L));
    }

    @Test
    void addCatToOwner_success() {
        Owner owner = new Owner(); owner.setId(1L); owner.setCats(new ArrayList<>());
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        ownerService.addCatToOwner(1L, 2L);
        assertEquals(1, owner.getCats().size());
        verify(ownerDao, times(1)).save(owner);
    }

    @Test
    void addCatToOwner_doesNotAddCat_alreadyAssigned() {
        Owner owner = new Owner(); owner.setId(1L);
        Cat cat = new Cat(); cat.setId(2L);
        owner.setCats(new ArrayList<>(List.of(cat)));
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));

        ownerService.addCatToOwner(1L, 2L);
        assertEquals(1, owner.getCats().size());
        verify(ownerDao, never()).save(owner);
    }

    @Test
    void addCatToOwner_throwsException_ownerNotFound() {
        when(ownerDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.addCatToOwner(1L, 2L));
    }

    @Test
    void removeCatFromOwner_success() {
        Owner owner = new Owner(); owner.setId(1L);
        Cat cat = new Cat(); cat.setId(2L);
        owner.setCats(new ArrayList<>(List.of(cat)));
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));

        ownerService.removeCatFromOwner(1L, 2L);
        assertEquals(0, owner.getCats().size());
        verify(ownerDao, times(1)).save(owner);
    }

    @Test
    void removeCatFromOwner_doesNothing_catNotFound() {
        Owner owner = new Owner(); owner.setId(1L); owner.setCats(new ArrayList<>());
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));

        ownerService.removeCatFromOwner(1L, 2L);
        assertEquals(0, owner.getCats().size());
        verify(ownerDao, times(1)).save(owner);
    }
}