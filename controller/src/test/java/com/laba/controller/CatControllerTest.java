package com.laba.controller;

import com.laba.ServiceException;
import com.laba.entity.Cat;
import com.laba.service.CatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CatController} using Mockito.
 * <p>
 * Tests methods of CatController.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CatControllerTest {

    @Mock
    private CatService catService;
    @InjectMocks
    private CatController catController;

//    @BeforeEach
//    void setUp() {
//        catService = mock(CatService.class);
//        catController = new CatController(catService);
//    }

    @Test
    void testCreateCatSuccess() {
        Cat cat = new Cat();
        assertDoesNotThrow(() -> catController.createCat(cat));
        verify(catService).saveCat(cat);
    }

    @Test
    void testCreateCatServiceException() {
        Cat cat = new Cat();
        doThrow(new ServiceException("service fail")).when(catService).saveCat(cat);
        assertDoesNotThrow(() -> catController.createCat(cat));
        verify(catService).saveCat(cat);
    }

    @Test
    void testCreateCatIllegalArgumentException() {
        Cat cat = new Cat();
        doThrow(new IllegalArgumentException("illegal")).when(catService).saveCat(cat);
        assertDoesNotThrow(() -> catController.createCat(cat));
        verify(catService).saveCat(cat);
    }

    @Test
    void testCreateCatUnexpectedException() {
        Cat cat = new Cat();
        doThrow(new RuntimeException("unexpected")).when(catService).saveCat(cat);
        assertDoesNotThrow(() -> catController.createCat(cat));
        verify(catService).saveCat(cat);
    }

    @Test
    void testUpdateCatSuccess() {
        Cat cat = new Cat();
        assertDoesNotThrow(() -> catController.updateCat(cat));
        verify(catService).updateCat(cat);
    }

    @Test
    void testUpdateCatExceptions() {
        Cat cat = new Cat();
        doThrow(new ServiceException("service fail")).when(catService).updateCat(cat);
        assertDoesNotThrow(() -> catController.updateCat(cat));

        doThrow(new IllegalArgumentException("illegal")).when(catService).updateCat(cat);
        assertDoesNotThrow(() -> catController.updateCat(cat));

        doThrow(new RuntimeException("unexpected")).when(catService).updateCat(cat);
        assertDoesNotThrow(() -> catController.updateCat(cat));
    }

    @Test
    void testDeleteCatByIdFound() {
        Cat cat = new Cat();
        when(catService.getCatById(1L)).thenReturn(cat);
        assertDoesNotThrow(() -> catController.deleteCatById(1L));
        verify(catService).getCatById(1L);
        verify(catService).deleteCat(cat);
    }

    @Test
    void testDeleteCatByIdNotFound() {
        when(catService.getCatById(1L)).thenReturn(null);
        assertDoesNotThrow(() -> catController.deleteCatById(1L));
        verify(catService).getCatById(1L);
        verify(catService, never()).deleteCat(any());
    }

    @Test
    void testDeleteCatByIdExceptions() {
        Cat cat = new Cat();
        when(catService.getCatById(1L)).thenReturn(cat);
        doThrow(new ServiceException("service fail")).when(catService).deleteCat(cat);
        assertDoesNotThrow(() -> catController.deleteCatById(1L));
        doThrow(new IllegalArgumentException("illegal")).when(catService).deleteCat(cat);
        assertDoesNotThrow(() -> catController.deleteCatById(1L));
        doThrow(new RuntimeException("unexpected")).when(catService).deleteCat(cat);
        assertDoesNotThrow(() -> catController.deleteCatById(1L));
    }

    @Test
    void testFindCatByIdFound() {
        Cat cat = new Cat();
        when(catService.getCatById(1L)).thenReturn(cat);
        Cat result = catController.findCatById(1L);
        assertEquals(cat, result);
    }

    @Test
    void testFindCatByIdNotFound() {
        when(catService.getCatById(1L)).thenReturn(null);
        Cat result = catController.findCatById(1L);
        assertNull(result);
    }

    @Test
    void testFindCatByIdExceptions() {
        doThrow(new ServiceException("service fail")).when(catService).getCatById(1L);
        assertNull(catController.findCatById(1L));
        doThrow(new IllegalArgumentException("illegal")).when(catService).getCatById(1L);
        assertNull(catController.findCatById(1L));
        doThrow(new RuntimeException("unexpected")).when(catService).getCatById(1L);
        assertNull(catController.findCatById(1L));
    }

    @Test
    void testFindAllCatsSuccess() {
        List<Cat> cats = List.of(new Cat(), new Cat());
        when(catService.getAllCats()).thenReturn(cats);

        List<Cat> result = catController.findAllCats();
        assertEquals(cats, result);
    }

    @Test
    void testFindAllCatsServiceException() {
        doThrow(new ServiceException("fail")).when(catService).getAllCats();
        List<Cat> result = catController.findAllCats();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindCatsByNameSuccess() {
        List<Cat> cats = List.of(new Cat());
        when(catService.findCatsByName("C")).thenReturn(cats);
        List<Cat> result = catController.findCatsByName("C");
        assertEquals(cats, result);
    }

    @Test
    void testFindCatsByNameExceptions() {
        doThrow(new ServiceException("fail")).when(catService).findCatsByName("C");
        assertTrue(catController.findCatsByName("C").isEmpty());
        doThrow(new IllegalArgumentException("illegal")).when(catService).findCatsByName("C");
        assertTrue(catController.findCatsByName("C").isEmpty());
    }

    @Test
    void testFindCatsByOwnerNameSuccess() {
        List<Cat> cats = List.of(new Cat());
        when(catService.findCatsByOwnerName("O")).thenReturn(cats);
        List<Cat> result = catController.findCatsByOwnerName("O");
        assertEquals(cats, result);
    }

    @Test
    void testFindCatsByOwnerNameExceptions() {
        doThrow(new ServiceException("fail")).when(catService).findCatsByOwnerName("O");
        assertTrue(catController.findCatsByOwnerName("O").isEmpty());
        doThrow(new IllegalArgumentException("illegal")).when(catService).findCatsByOwnerName("O");
        assertTrue(catController.findCatsByOwnerName("O").isEmpty());
    }

    @Test
    void testAddFriendSuccess() {
        assertDoesNotThrow(() -> catController.addFriend(1L, 2L));
        verify(catService).addFriend(1L, 2L);
    }

    @Test
    void testAddFriendExceptions() {
        doThrow(new ServiceException("fail")).when(catService).addFriend(1L, 2L);
        assertDoesNotThrow(() -> catController.addFriend(1L, 2L));
        doThrow(new IllegalArgumentException("illegal")).when(catService).addFriend(1L, 2L);
        assertDoesNotThrow(() -> catController.addFriend(1L, 2L));
    }
}