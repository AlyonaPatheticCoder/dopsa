package com.laba.impl;

import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import com.laba.validation.Validation;
import com.sun.tools.javac.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests foe OwnerService.
 */
//@SpringBootTest(classes = com.laba.app.)
//@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

    @Mock
    private OwnerDao ownerDao;

    @Mock
    private CatDao catDao;

    private OwnerServiceImpl ownerService;

    private Owner owner;
    private Cat cat;

    private Validation validation;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1L);
        owner.setName("O");
        owner.setBirthday(LocalDate.of(1988, 1, 1));
        owner.setCats(new ArrayList<>());

        cat = new Cat();
        cat.setId(2L);
        cat.setName("C");
        cat.setBirthday(LocalDate.of(2025, 1, 1));
        cat.setBreed("B");
        cat.setColor(Color.BLACK);

        validation = new Validation();
        validation.getOwner().setName(50);
        validation.getCat().setName(50);
        validation.getCat().setBreed(50);
        ownerService = new OwnerServiceImpl(ownerDao, catDao, validation);
    }

    /**
     * Gets owner by id valid id returns dto.
     */
    @Test
    void getOwnerById_validId_returnsDto() {
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        OwnerDto result = ownerService.getOwnerById(1L);
        assertNotNull(result);
        assertEquals(owner.getId(), result.getId());
        verify(ownerDao).findById(1L);
    }

    /**
     * Gets owner by id null id throws exception.
     */
    @Test
    void getOwnerById_nullId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.getOwnerById(null));
        verifyNoInteractions(ownerDao);
    }

    /**
     * Gets owner by id not found throws exception.
     */
    @Test
    void getOwnerById_notFound_throwsException() {
        when(ownerDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.getOwnerById(1L));
    }

    /**
     * Gets all owners returns list.
     */
    @Test
    void getAllOwners_returnsList() {
        when(ownerDao.findAll()).thenReturn(List.of(owner));
        List<OwnerDto> result = ownerService.getAllOwners();
        assertEquals(1, result.size());
        assertEquals("O", result.get(0).getName());
    }

    /**
     * Find owners by name valid name returns list.
     */
    @Test
    void findOwnersByName_validName_returnsList() {
        when(ownerDao.findByNameIgnoreCase("o")).thenReturn(List.of(owner));
        List<OwnerDto> result = ownerService.findOwnersByName("o");
        assertEquals(1, result.size());
        verify(ownerDao).findByNameIgnoreCase("o");
    }

    /**
     * Find owners by name invalid name throws exception.
     */
    @Test
    void findOwnersByName_invalidName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByName("  "));
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByName(null));
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByName("O".repeat(51)));
    }

    /**
     * Find owners by cat name valid name returns list.
     */
    @Test
    void findOwnersByCatName_validName_returnsList() {
        when(ownerDao.findByCats_NameIgnoreCase("c")).thenReturn(List.of(owner));
        List<OwnerDto> result = ownerService.findOwnersByCatName("c");
        assertEquals(1, result.size());
        verify(ownerDao).findByCats_NameIgnoreCase("c");
    }

    /**
     * Find owners by cat name invalid name throws exception.
     */
    @Test
    void findOwnersByCatName_invalidName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByCatName(""));
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByCatName(null));
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByCatName("C".repeat(51)));
    }

    /**
     * Find owner by cat id valid returns owner.
     */
    @Test
    void findOwnerByCatId_valid_returnsOwner() {
        when(ownerDao.findByCats_Id(2L)).thenReturn(Optional.of(owner));
        OwnerDto result = ownerService.findOwnerByCatId(2L);
        assertNotNull(result);
        assertEquals(owner.getName(), result.getName());
    }

    /**
     * Find owner by cat id null throws exception.
     */
    @Test
    void findOwnerByCatId_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerByCatId(null));
    }

    /**
     * Find owner by cat id not found throws exception.
     */
    @Test
    void findOwnerByCatId_notFound_throwsException() {
        when(ownerDao.findByCats_Id(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerByCatId(2L));
    }

    /**
     * Save owner valid saves owner.
     */
    @Test
    void saveOwner_valid_savesOwner() {
        OwnerDto dto = OwnerDto.fromEntity(owner);
        when(ownerDao.save(any())).thenReturn(owner);
        ownerService.saveOwner(dto);
        verify(ownerDao).save(any(Owner.class));
    }

    /**
     * Save owner null throws exception.
     */
    @Test
    void saveOwner_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(null));
    }

    /**
     * Save owner invalid birthday throws exception.
     */
    @Test
    void saveOwner_invalidBirthday_throwsException() {
        Owner badOwner = new Owner();
        badOwner.setId(1L);
        badOwner.setName("BO");
        badOwner.setBirthday(LocalDate.now().plusDays(1));
        OwnerDto dto = OwnerDto.fromEntity(badOwner);
        assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(dto));
    }

    /**
     * Update owner valid updates owner.
     */
    @Test
    void updateOwner_valid_updatesOwner() {
        OwnerDto dto = OwnerDto.fromEntity(owner);
        when(ownerDao.save(any())).thenReturn(owner);
        ownerService.updateOwner(dto);
        verify(ownerDao).save(any(Owner.class));
    }

    /**
     * Update owner null throws exception.
     */
    @Test
    void updateOwner_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateOwner(null));
    }

    /**
     * Update owner missing id throws exception.
     */
    @Test
    void updateOwner_missingId_throwsException() {
        OwnerDto dto = new OwnerDto();
        dto.setName("O");
        dto.setBirthday(LocalDate.of(1988, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateOwner(dto));
    }

    /**
     * Delete owner valid deletes owner.
     */
    @Test
    void deleteOwner_valid_deletesOwner() {
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        ownerService.deleteOwner(1L);
        verify(ownerDao).delete(owner);
    }

    /**
     * Delete owner null id throws exception.
     */
    @Test
    void deleteOwner_nullId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.deleteOwner(null));
    }

    /**
     * Delete owner owner not found throws exception.
     */
    @Test
    void deleteOwner_ownerNotFound_throwsException() {
        when(ownerDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.deleteOwner(1L));
    }

    /**
     * Delete owner with cats throws exception.
     */
    @Test
    void deleteOwner_withCats_throwsException() {
        owner.getCats().add(cat);
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        assertThrows(IllegalStateException.class, () -> ownerService.deleteOwner(1L));
    }

    /**
     * Add cat to owner valid adds cat.
     */
    @Test
    void addCatToOwner_valid_addsCat() {
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        ownerService.addCatToOwner(1L, 2L);
        assertTrue(owner.getCats().contains(cat));
        assertEquals(owner, cat.getOwner());
        verify(catDao).save(cat);
        verify(ownerDao).save(owner);
    }

    /**
     * Add cat to owner cat already has another owner throws exception.
     */
    @Test
    void addCatToOwner_catAlreadyHasAnotherOwner_throwsException() {
        Owner other = new Owner();
        other.setId(99L);
        cat.setOwner(other);
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        assertThrows(IllegalStateException.class, () -> ownerService.addCatToOwner(1L, 2L));
    }

    /**
     * Remove cat from owner valid removes cat.
     */
    @Test
    void removeCatFromOwner_valid_removesCat() {
        owner.getCats().add(cat);
        cat.setOwner(owner);
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        ownerService.removeCatFromOwner(1L, 2L);
        assertFalse(owner.getCats().contains(cat));
        assertNull(cat.getOwner());
        verify(catDao).save(cat);
        verify(ownerDao).save(owner);
    }

    /**
     * Remove cat from owner cat not in owner throws exception.
     */
    @Test
    void removeCatFromOwner_catNotInOwner_throwsException() {
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        assertThrows(IllegalArgumentException.class, () -> ownerService.removeCatFromOwner(1L, 2L));
    }
}