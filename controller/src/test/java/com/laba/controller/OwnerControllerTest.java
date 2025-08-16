package com.laba.controller;

import com.laba.ServiceException;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import com.laba.service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OwnerController} using Mockito.
 * <p>
 * Tests methods of CatController.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;
    @InjectMocks
    private OwnerController ownerController;

    @Test
    void testCreateOwnerSuccess() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> ownerController.createOwner(owner));
        verify(ownerService).saveOwner(owner);
    }

    @Test
    void testCreateOwnerExceptions() {
        Owner owner = new Owner();

        doThrow(new ServiceException("service fail")).when(ownerService).saveOwner(owner);
        assertDoesNotThrow(() -> ownerController.createOwner(owner));

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).saveOwner(owner);
        assertDoesNotThrow(() -> ownerController.createOwner(owner));

        doThrow(new RuntimeException("unexpected")).when(ownerService).saveOwner(owner);
        assertDoesNotThrow(() -> ownerController.createOwner(owner));
    }

    @Test
    void testUpdateOwnerSuccess() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> ownerController.updateOwner(owner));
        verify(ownerService).updateOwner(owner);
    }

    @Test
    void testUpdateOwnerExceptions() {
        Owner owner = new Owner();

        doThrow(new ServiceException("service fail")).when(ownerService).updateOwner(owner);
        assertDoesNotThrow(() -> ownerController.updateOwner(owner));

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).updateOwner(owner);
        assertDoesNotThrow(() -> ownerController.updateOwner(owner));

        doThrow(new RuntimeException("unexpected")).when(ownerService).updateOwner(owner);
        assertDoesNotThrow(() -> ownerController.updateOwner(owner));
    }

    @Test
    void testDeleteOwnerByIdFound() {
        Owner owner = new Owner();
        when(ownerService.getOwnerById(1L)).thenReturn(owner);

        assertDoesNotThrow(() -> ownerController.deleteOwnerById(1L));
        verify(ownerService).getOwnerById(1L);
        verify(ownerService).deleteOwner(owner);
    }

    @Test
    void testDeleteOwnerByIdNotFound() {
        when(ownerService.getOwnerById(1L)).thenReturn(null);

        assertDoesNotThrow(() -> ownerController.deleteOwnerById(1L));
        verify(ownerService).getOwnerById(1L);
        verify(ownerService, never()).deleteOwner(any());
    }

    @Test
    void testDeleteOwnerByIdExceptions() {
        Owner owner = new Owner();
        when(ownerService.getOwnerById(1L)).thenReturn(owner);

        doThrow(new ServiceException("service fail")).when(ownerService).deleteOwner(owner);
        assertDoesNotThrow(() -> ownerController.deleteOwnerById(1L));

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).deleteOwner(owner);
        assertDoesNotThrow(() -> ownerController.deleteOwnerById(1L));

        doThrow(new RuntimeException("unexpected")).when(ownerService).deleteOwner(owner);
        assertDoesNotThrow(() -> ownerController.deleteOwnerById(1L));
    }

    @Test
    void testFindOwnerByIdFound() {
        Owner owner = new Owner();
        when(ownerService.getOwnerById(1L)).thenReturn(owner);

        Owner result = ownerController.findOwnerById(1L);
        assertEquals(owner, result);
    }

    @Test
    void testFindOwnerByIdNotFound() {
        when(ownerService.getOwnerById(1L)).thenReturn(null);

        Owner result = ownerController.findOwnerById(1L);
        assertNull(result);
    }

    @Test
    void testFindOwnerByIdExceptions() {
        doThrow(new ServiceException("service fail")).when(ownerService).getOwnerById(1L);
        assertNull(ownerController.findOwnerById(1L));

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).getOwnerById(1L);
        assertNull(ownerController.findOwnerById(1L));

        doThrow(new RuntimeException("unexpected")).when(ownerService).getOwnerById(1L);
        assertNull(ownerController.findOwnerById(1L));
    }

    @Test
    void testFindAllOwnersSuccess() {
        List<Owner> owners = List.of(new Owner(), new Owner());
        when(ownerService.getAllOwners()).thenReturn(owners);

        List<Owner> result = ownerController.findAllOwners();
        assertEquals(owners, result);
    }

    @Test
    void testFindAllOwnersExceptions() {

        doThrow(new ServiceException("fail")).when(ownerService).getAllOwners();
        assertTrue(ownerController.findAllOwners().isEmpty());

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).getAllOwners();
        assertTrue(ownerController.findAllOwners().isEmpty());

        doThrow(new RuntimeException("unexpected")).when(ownerService).getAllOwners();
        assertTrue(ownerController.findAllOwners().isEmpty());
    }

    @Test
    void testFindOwnersByNameSuccess() {
        List<Owner> owners = List.of(new Owner());
        when(ownerService.findOwnersByName("O")).thenReturn(owners);

        List<Owner> result = ownerController.findOwnersByName("O");
        assertEquals(owners, result);
    }

    @Test
    void testFindOwnersByNameExceptions() {
        doThrow(new ServiceException("fail")).when(ownerService).findOwnersByName("O");
        assertTrue(ownerController.findOwnersByName("O").isEmpty());

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).findOwnersByName("O");
        assertTrue(ownerController.findOwnersByName("O").isEmpty());
    }

    @Test
    void testFindOwnersByCatNameSuccess() {
        List<Owner> owners = List.of(new Owner());
        when(ownerService.findOwnersByCatName("C")).thenReturn(owners);

        List<Owner> result = ownerController.findOwnersByCatName("C");
        assertEquals(owners, result);
    }

    @Test
    void testFindOwnersByCatNameExceptions() {
        doThrow(new ServiceException("fail")).when(ownerService).findOwnersByCatName("C");
        assertTrue(ownerController.findOwnersByCatName("C").isEmpty());

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).findOwnersByCatName("C");
        assertTrue(ownerController.findOwnersByCatName("C").isEmpty());
    }

    @Test
    void testFindOwnerByCatIdSuccess() {
        Owner owner = new Owner();
        when(ownerService.findOwnerByCatId(5L)).thenReturn(owner);

        Owner result = ownerController.findOwnerByCatId(5L);
        assertEquals(owner, result);
    }

    @Test
    void testFindOwnerByCatIdExceptions() {
        doThrow(new ServiceException("fail")).when(ownerService).findOwnerByCatId(5L);
        assertNull(ownerController.findOwnerByCatId(5L));

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).findOwnerByCatId(5L);
        assertNull(ownerController.findOwnerByCatId(5L));
    }

    @Test
    void testGetCatsByOwnerIdSuccess() {
        List<Cat> cats = List.of(new Cat(), new Cat());
        when(ownerService.getCatsByOwnerId(1L)).thenReturn(cats);

        List<Cat> result = ownerController.getCatsByOwnerId(1L);
        assertEquals(cats, result);
    }

    @Test
    void testGetCatsByOwnerIdExceptions() {
        doThrow(new ServiceException("fail")).when(ownerService).getCatsByOwnerId(1L);
        assertTrue(ownerController.getCatsByOwnerId(1L).isEmpty());

        doThrow(new IllegalArgumentException("illegal")).when(ownerService).getCatsByOwnerId(1L);
        assertTrue(ownerController.getCatsByOwnerId(1L).isEmpty());
    }
}