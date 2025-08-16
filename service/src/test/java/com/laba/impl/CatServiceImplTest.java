package com.laba.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.laba.DaoException;
import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests for {@link CatServiceImpl} using Mockito.
 * <p>
 * Tests methods of CatService.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CatServiceImplTest {

    @Mock
    private CatDao catDao;

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private CatServiceImpl catService;

    private Owner owner;
    private Cat cat;

    @BeforeEach
    void setUp() {
        owner = new Owner.Builder()
                .name("O")
                .build();
        owner.setId(1L);

        cat = new Cat.Builder()
                .name("C")
                .birthday(LocalDate.of(2022, 1, 1))
                .color(Color.WHITE)
                .breed("B")
                .owner(owner)
                .build();
        cat.setId(1L);
    }

    @Test
    void saveCat_success() throws DaoException {
        when(ownerDao.findById(owner.getId())).thenReturn(owner);
        doNothing().when(catDao).save(cat);
        assertDoesNotThrow(() -> catService.saveCat(cat));
        verify(catDao).save(cat);
    }

    @Test
    void saveCat_ownerDoesNotExist_throwsIllegalArgumentException() throws DaoException {
        when(ownerDao.findById(owner.getId())).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullCat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(null));
    }

    @Test
    void saveCat_nullName_throwsIllegalArgumentException() throws DaoException {
        cat.setName(null);
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_emptyName_throwsIllegalArgumentException() throws DaoException {
        cat.setName("  ");
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }


    @Test
    void saveCat_nameTooLong_throwsIllegalArgumentException() throws DaoException {
        cat.setName("C".repeat(31));
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullBirthday_throwsIllegalArgumentException() throws DaoException {
        cat.setBirthday(null);
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_futureBirthday_throwsIllegalArgumentException() throws DaoException {
        cat.setBirthday(LocalDate.now().plusDays(1));
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullColor_throwsIllegalArgumentException() throws DaoException {
        cat.setColor(null);
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullOwner_throwsIllegalArgumentException() {
        cat.setOwner(null);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullOwnerId_throwsIllegalArgumentException() {
        cat.getOwner().setId(null);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_nullBreed_throwsIllegalArgumentException() throws DaoException {
        cat.setBreed(null);
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void saveCat_emptyBreed_throwsIllegalArgumentException() throws DaoException {
        cat.setBreed("   ");
        //when(ownerDao.findById(owner.getId())).thenReturn(owner);
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(cat));
    }

    @Test
    void updateCat_success() throws DaoException {
        when(ownerDao.findById(owner.getId())).thenReturn(owner);
        doNothing().when(catDao).update(cat);
        assertDoesNotThrow(() -> catService.updateCat(cat));
        verify(catDao).update(cat);
    }

    @Test
    void updateCat_nullCat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.updateCat(null));
    }

    @Test
    void updateCat_nullId_throwsIllegalArgumentException() {
        cat.setId(null);
        assertThrows(IllegalArgumentException.class, () -> catService.updateCat(cat));
    }

    @Test
    void deleteCat_success() throws DaoException {
        doNothing().when(catDao).delete(cat);
        assertDoesNotThrow(() -> catService.deleteCat(cat));
        verify(catDao).delete(cat);
    }

    @Test
    void deleteCat_nullCat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.deleteCat(null));
    }

    @Test
    void getCatById_success() throws DaoException {
        when(catDao.findById(1L)).thenReturn(cat);
        Cat result = catService.getCatById(1L);
        assertEquals(cat, result);
        verify(catDao).findById(1L);
    }

    @Test
    void getCatById_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.getCatById(null));
    }

    @Test
    void getAllCats_success() throws DaoException {
        List<Cat> cats = Collections.singletonList(cat);
        when(catDao.findAll()).thenReturn(cats);
        List<Cat> result = catService.getAllCats();
        assertEquals(cats, result);
        verify(catDao).findAll();
    }

    @Test
    void findCatsByName_success() throws DaoException {
        List<Cat> cats = Collections.singletonList(cat);
        when(catDao.findByName("C")).thenReturn(cats);
        List<Cat> result = catService.findCatsByName("C");
        assertEquals(cats, result);
        verify(catDao).findByName("C");
    }

    @Test
    void findCatsByName_nullOrEmpty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByName(null));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByName(" "));
    }

    @Test
    void findCatsByOwnerName_success() throws DaoException {
        List<Cat> cats = Collections.singletonList(cat);
        when(catDao.findByOwnerName("O")).thenReturn(cats);

        List<Cat> result = catService.findCatsByOwnerName("O");
        assertEquals(cats, result);
        verify(catDao).findByOwnerName("O");
    }

    @Test
    void findCatsByOwnerName_nullOrEmpty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerName(null));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerName(" "));
    }

    @Test
    void findCatsByOwnerId_success() throws DaoException {
        List<Cat> cats = Collections.singletonList(cat);
        when(catDao.findByOwnerId(1L)).thenReturn(cats);
        List<Cat> result = catService.findCatsByOwnerId(1L);
        assertEquals(cats, result);
        verify(catDao).findByOwnerId(1L);
    }

    @Test
    void findCatsByOwnerId_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerId(null));
    }

    @Test
    void findCatsByNameAndOwner_success() throws DaoException {
        List<Cat> cats = Collections.singletonList(cat);
        when(catDao.findByNameAndOwner("C", "O")).thenReturn(cats);

        List<Cat> result = catService.findCatsByNameAndOwner("C", "O");
        assertEquals(cats, result);
        verify(catDao).findByNameAndOwner("C", "O");
    }

    @Test
    void findCatsByNameAndOwner_nullOrEmpty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByNameAndOwner(null, "O"));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByNameAndOwner("C", null));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByNameAndOwner(" ", "O"));
    }

    @Test
    void addFriend_success() throws DaoException {

        Cat friend = new Cat.Builder()
                .name("F")
                .birthday(LocalDate.of(2024, 2, 2))
                .color(Color.BROWN)
                .breed("B")
                .owner(owner)
                .build();

        friend.setId(2L);
        when(catDao.findById(1L)).thenReturn(cat);
        when(catDao.findById(2L)).thenReturn(friend);

        catService.addFriend(1L, 2L);

        assertTrue(cat.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(cat));
        verify(catDao).update(cat);
        verify(catDao).update(friend);
    }

    @Test
    void addFriend_sameId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.addFriend(1L, 1L));
    }

    @Test
    void addFriend_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.addFriend(null, 2L));
        assertThrows(IllegalArgumentException.class, () -> catService.addFriend(1L, null));
    }

    @Test
    void removeFriend_success() throws DaoException {
        Cat friend = new Cat();
        friend.setId(2L);
        friend.setFriends(new ArrayList<>());
        cat.setFriends(new ArrayList<>(List.of(friend)));
        friend.getFriends().add(cat);
        when(catDao.findById(1L)).thenReturn(cat);
        when(catDao.findById(2L)).thenReturn(friend);
        catService.removeFriend(1L, 2L);

        assertFalse(cat.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(cat));
        verify(catDao).update(cat);
        verify(catDao).update(friend);
    }

    @Test
    void getFriends_success() throws DaoException {
        cat.setFriends(new ArrayList<>());
        when(catDao.findById(1L)).thenReturn(cat);
        List<Cat> friends = catService.getFriends(1L);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
    void getFriends_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> catService.getFriends(null));
    }
}