package com.laba.impl;
import com.laba.DaoException;
import com.laba.ServiceException;
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
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OwnerServiceImpl} using Mockito.
 * <p>
 * Tests methods of CatService.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    private Owner validOwner;

    @BeforeEach
    void setUp() {

        validOwner = new Owner.Builder()
                .name("O")
                .birthday(LocalDate.of(1988, 1, 1))
                .build();
        validOwner.setId(1L);
    }

    @Test
    void saveOwner_valid() throws DaoException {
        doNothing().when(ownerDao).save(validOwner);
        assertDoesNotThrow(() -> ownerService.saveOwner(validOwner));
        verify(ownerDao).save(validOwner);
    }

    @Test
    void saveOwner_nullOwner_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(null));
        verifyNoInteractions(ownerDao);
    }

    @Test
    void saveOwner_invalidName_throws() {
        validOwner.setName("   ");
        assertThrows(IllegalArgumentException.class, () -> ownerService.saveOwner(validOwner));
    }

    @Test
    void saveOwner_daoError_throwsServiceException() throws DaoException {
        doThrow(new DaoException("DAO fail")).when(ownerDao).save(any());
        assertThrows(ServiceException.class, () -> ownerService.saveOwner(validOwner));
    }
    @Test
    void updateOwner_valid() throws DaoException {
        ownerService.updateOwner(validOwner);
        verify(ownerDao).update(validOwner);
    }

    @Test
    void updateOwner_nullId_throws() {
        validOwner.setId(null);
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateOwner(validOwner));
    }

    @Test
    void updateOwner_daoError_throwsServiceException() throws DaoException {
        doThrow(new DaoException("DAO fail")).when(ownerDao).update(any());
        assertThrows(ServiceException.class, () -> ownerService.updateOwner(validOwner));
    }

    @Test
    void deleteOwner_valid() throws DaoException {
        when(ownerDao.findById(1L)).thenReturn(validOwner);
        ownerService.deleteOwner(validOwner);
        verify(ownerDao).delete(validOwner);
    }

    @Test
    void deleteOwner_nullOwner_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.deleteOwner(null));
    }

    @Test
    void deleteOwner_nullId_throws() {
        validOwner.setId(null);
        assertThrows(IllegalArgumentException.class, () -> ownerService.deleteOwner(validOwner));
    }

    @Test
    void deleteOwner_notFound_throws() throws DaoException {
        when(ownerDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> ownerService.deleteOwner(validOwner));
    }

    @Test
    void deleteOwner_withCats_throws() throws DaoException {
        validOwner.setCats(List.of(new Cat()));
        when(ownerDao.findById(1L)).thenReturn(validOwner);
        assertThrows(IllegalStateException.class, () -> ownerService.deleteOwner(validOwner));
    }

    @Test
    void deleteOwner_daoErrorFind_throwsServiceException() throws DaoException {
        when(ownerDao.findById(1L)).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.deleteOwner(validOwner));
    }

    @Test
    void deleteOwner_daoErrorDelete_throwsServiceException() throws DaoException {
        when(ownerDao.findById(1L)).thenReturn(validOwner);
        doThrow(new DaoException("fail")).when(ownerDao).delete(validOwner);
        assertThrows(ServiceException.class, () -> ownerService.deleteOwner(validOwner));
    }

    @Test
    void getOwnerById_valid() throws DaoException {
        when(ownerDao.findById(1L)).thenReturn(validOwner);
        Owner result = ownerService.getOwnerById(1L);
        assertEquals(validOwner, result);
    }

    @Test
    void getOwnerById_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.getOwnerById(null));
    }

    @Test
    void getOwnerById_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findById(1L)).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.getOwnerById(1L));
    }

    @Test
    void getAllOwners_valid() throws DaoException {
        when(ownerDao.findAll()).thenReturn(List.of(validOwner));
        List<Owner> result = ownerService.getAllOwners();
        assertEquals(1, result.size());
    }

    @Test
    void getAllOwners_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findAll()).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.getAllOwners());
    }
    @Test
    void findOwnersByName_valid() throws DaoException {
        when(ownerDao.findByName("O")).thenReturn(List.of(validOwner));
        List<Owner> result = ownerService.findOwnersByName("O");
        assertEquals(1, result.size());
    }

    @Test
    void findOwnersByName_nullName_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByName(null));
    }

    @Test
    void findOwnersByName_emptyName_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByName(" "));
    }

    @Test
    void findOwnersByName_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findByName("O")).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.findOwnersByName("O"));
    }

    @Test
    void findOwnersByCatName_valid() throws DaoException {
        when(ownerDao.findByCatName("C")).thenReturn(List.of(validOwner));
        List<Owner> result = ownerService.findOwnersByCatName("C");
        assertEquals(1, result.size());
    }

    @Test
    void findOwnersByCatName_nullName_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByCatName(null));
    }

    @Test
    void findOwnersByCatName_emptyName_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnersByCatName(" "));
    }

    @Test
    void findOwnersByCatName_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findByCatName("C")).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.findOwnersByCatName("C"));
    }

    @Test
    void findOwnerByCatId_valid() throws DaoException {
        when(ownerDao.findByCatId(2L)).thenReturn(validOwner);
        Owner result = ownerService.findOwnerByCatId(2L);
        assertEquals(validOwner, result);
    }

    @Test
    void findOwnerByCatId_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerByCatId(null));
    }

    @Test
    void findOwnerByCatId_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findByCatId(2L)).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.findOwnerByCatId(2L));
    }

    @Test
    void getCatsByOwnerId_valid() throws DaoException {
        validOwner.setCats(Collections.singletonList(new Cat()));
        when(ownerDao.findById(1L)).thenReturn(validOwner);
        List<Cat> cats = ownerService.getCatsByOwnerId(1L);
        assertEquals(1, cats.size());
    }

    @Test
    void getCatsByOwnerId_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> ownerService.getCatsByOwnerId(null));
    }

    @Test
    void getCatsByOwnerId_notFound_throws() throws DaoException {
        when(ownerDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> ownerService.getCatsByOwnerId(1L));
    }

    @Test
    void getCatsByOwnerId_daoError_throwsServiceException() throws DaoException {
        when(ownerDao.findById(1L)).thenThrow(new DaoException("fail"));
        assertThrows(ServiceException.class, () -> ownerService.getCatsByOwnerId(1L));
    }
}