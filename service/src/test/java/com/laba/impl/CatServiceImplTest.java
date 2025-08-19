package com.laba.impl;

import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.dto.CatDto;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests foe CatService.
 */

@ExtendWith(MockitoExtension.class)
class CatServiceImplTest {
    @Mock
    private CatDao catDao;
    @Mock
    private OwnerDao ownerDao;
    @InjectMocks
    private CatServiceImpl catService;

    private Cat cat;
    private CatDto catDto;
    private Owner owner;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1L);
        owner.setName("O");

        cat = new Cat();
        cat.setId(1L);
        cat.setName("C");
        cat.setBreed("B");
        cat.setColor(Color.BLACK);
        cat.setBirthday(LocalDate.of(2022, 2, 24));
        cat.setOwner(owner);
        cat.setFriends(new ArrayList<>());
        catDto = CatDto.fromEntity(cat);

        ReflectionTestUtils.setField(catService, "maxOwnerNameLength", 50);
        ReflectionTestUtils.setField(catService, "maxCatNameLength", 50);
        ReflectionTestUtils.setField(catService, "maxCatBreedLength", 50);
    }

    /**
     * Test get cat by id found.
     */
    @Test
    void testGetCatByIdFound() {
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        CatDto result = catService.getCatById(1L);
        assertNotNull(result);
        assertEquals(cat.getName(), result.getName());
    }

    /**
     * Test get cat by id not found.
     */
    @Test
    void testGetCatByIdNotFound() {
        when(catDao.findById(2L)).thenReturn(Optional.empty());
        CatDto result = catService.getCatById(2L);
        assertNull(result);
    }

    /**
     * Test get cat by id null.
     */
    @Test
    void testGetCatByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> catService.getCatById(null));
    }

    /**
     * Test get all cats.
     */
    @Test
    void testGetAllCats() {
        when(catDao.findAll()).thenReturn(List.of(cat));
        List<CatDto> result = catService.getAllCats();
        assertEquals(1, result.size());
        assertEquals(cat.getName(), result.get(0).getName());
    }

    /**
     * Test find cats by name.
     */
    @Test
    void testFindCatsByName() {
        when(catDao.findByNameIgnoreCase("C")).thenReturn(List.of(cat));
        List<CatDto> result = catService.findCatsByName("C");
        assertEquals(1, result.size());
    }

    /**
     * Test find cats by name invalid.
     */
    @Test
    void testFindCatsByNameInvalid() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByName(null));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByName("   "));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByName("C".repeat(51)));
    }

    /**
     * Test find cats by owner name.
     */
    @Test
    void testFindCatsByOwnerName() {
        when(catDao.findByOwner_NameIgnoreCase("O")).thenReturn(List.of(cat));
        List<CatDto> result = catService.findCatsByOwnerName("O");
        assertEquals(1, result.size());
    }

    /**
     * Test find cats by owner name invalid.
     */
    @Test
    void testFindCatsByOwnerNameInvalid() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerName(null));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerName("   "));
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerName("O".repeat(51)));
    }

    /**
     * Test find cats by owner id.
     */
    @Test
    void testFindCatsByOwnerId() {
        when(catDao.findByOwner_Id(1L)).thenReturn(List.of(cat));
        List<CatDto> result = catService.findCatsByOwnerId(1L);
        assertEquals(1, result.size());
    }

    /**
     * Test find cats by owner id null.
     */
    @Test
    void testFindCatsByOwnerIdNull() {
        assertThrows(IllegalArgumentException.class, () -> catService.findCatsByOwnerId(null));
    }

    /**
     * Test find cats by filter null.
     */
// findCatsByFilter
    @Test
    void testFindCatsByFilterNull() {
        when(catDao.findAll()).thenReturn(List.of(cat));
        List<CatDto> result = catService.findCatsByFilter(null);
        assertEquals(1, result.size());
    }

    /**
     * Test find cats by filter with fields.
     */
    @Test
    void testFindCatsByFilterWithFields() {
        CatDto filter = new CatDto();
        filter.setName("C");
        when(catDao.findAll(any(Specification.class))).thenReturn(List.of(cat));
        List<CatDto> result = catService.findCatsByFilter(filter);
        assertEquals(1, result.size());
    }

    /**
     * Test save cat success.
     */
    @Test
    void testSaveCatSuccess() {
        when(ownerDao.existsById(1L)).thenReturn(true);
        catService.saveCat(catDto);
        verify(catDao, times(1)).save(any(Cat.class));
    }

    /**
     * Test save cat null.
     */
    @Test
    void testSaveCatNull() {
        assertThrows(IllegalArgumentException.class, () -> catService.saveCat(null));
    }

    /**
     * Test update cat success.
     */
    @Test
    void testUpdateCatSuccess() {
        when(ownerDao.existsById(1L)).thenReturn(true);
        catService.updateCat(catDto);
        verify(catDao, times(1)).save(any(Cat.class));
    }

    /**
     * Test update cat null.
     */
    @Test
    void testUpdateCatNull() {
        assertThrows(IllegalArgumentException.class, () -> catService.updateCat(null));
    }

    /**
     * Test delete cat success.
     */
    @Test
    void testDeleteCatSuccess() {
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        catService.deleteCat(1L);
        verify(catDao, times(1)).delete(cat);
    }

    /**
     * Test delete cat not found.
     */
    @Test
    void testDeleteCatNotFound() {
        when(catDao.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> catService.deleteCat(2L));
    }

    /**
     * Test delete cat null.
     */
    @Test
    void testDeleteCatNull() {
        assertThrows(IllegalArgumentException.class, () -> catService.deleteCat(null));
    }

    /**
     * Test add friend success.
     */
    @Test
    void testAddFriendSuccess() {
        Cat friend = new Cat();
        friend.setId(2L);
        friend.setFriends(new ArrayList<>());
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        when(catDao.findById(2L)).thenReturn(Optional.of(friend));
        catService.addFriend(1L, 2L);
        assertTrue(cat.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(cat));
        verify(catDao, times(2)).save(any(Cat.class));
    }

    /**
     * Test add friend self.
     */
    @Test
    void testAddFriendSelf() {
        assertThrows(IllegalArgumentException.class, () -> catService.addFriend(1L, 1L));
    }

    /**
     * Test add friend already friends.
     */
    @Test
    void testAddFriendAlreadyFriends() {
        Cat friend = new Cat();
        friend.setId(2L);
        cat.getFriends().add(friend);
        friend.setFriends(new ArrayList<>(Set.of(cat)));
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        when(catDao.findById(2L)).thenReturn(Optional.of(friend));
        assertThrows(IllegalStateException.class, () -> catService.addFriend(1L, 2L));
    }

    /**
     * Test remove friend success.
     */
    @Test
    void testRemoveFriendSuccess() {
        Cat friend = new Cat();
        friend.setId(2L);
        cat.getFriends().add(friend);
        friend.setFriends(new ArrayList<>(Set.of(cat)));
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        when(catDao.findById(2L)).thenReturn(Optional.of(friend));
        catService.removeFriend(1L, 2L);
        assertFalse(cat.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(cat));
    }

    /**
     * Test remove friend not friends.
     */
    @Test
    void testRemoveFriendNotFriends() {
        Cat friend = new Cat();
        friend.setId(2L);
        when(catDao.findById(1L)).thenReturn(Optional.of(cat));
        when(catDao.findById(2L)).thenReturn(Optional.of(friend));
        assertThrows(IllegalArgumentException.class, () -> catService.removeFriend(1L, 2L));
    }
}