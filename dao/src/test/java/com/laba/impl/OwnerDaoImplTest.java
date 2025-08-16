package com.laba.impl;
import com.laba.DaoException;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OwnerDaoImpl} using Mockito.
 * <p>
 * Tests methods of OwnerDAO.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class OwnerDaoImplTest {

    @Mock private SessionFactory sessionFactory;
    @Mock private Session session;
    @Mock private Transaction transaction;

    @InjectMocks
    private OwnerDaoImpl ownerDao;

    @BeforeEach
    void setUp() {
        lenient().when(sessionFactory.openSession()).thenReturn(session);
        lenient().when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    void testSave_OwnerSaved() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> ownerDao.save(owner));
        verify(session).persist(owner);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testSave_NullOwner_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.save(null));
    }

    @Test
    void testUpdate_OwnerUpdated() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> ownerDao.update(owner));
        verify(session).merge(owner);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testUpdate_NullOwner_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.update(null));
    }

    @Test
    void testDelete_OwnerDeleted() {
        Owner owner = new Owner();
        owner.setId(1L);
        when(session.get(Owner.class, 1L)).thenReturn(owner);

        assertDoesNotThrow(() -> ownerDao.delete(owner));
        verify(session).remove(owner);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    void testDelete_OwnerNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.delete(null));
    }

    @Test
    void testDelete_OwnerIdNull_ThrowsException() {
        Owner owner = new Owner();
        assertThrows(IllegalArgumentException.class, () -> ownerDao.delete(owner));
    }

    @Test
    void testDelete_OwnerNotFound_ThrowsDaoException() {
        Owner owner = new Owner();
        owner.setId(1L);
        when(session.get(Owner.class, 1L)).thenReturn(null);
        assertThrows(DaoException.class, () -> ownerDao.delete(owner));
    }

    @Test
    void testFindById_ReturnsOwner() {
        Owner owner = new Owner();
        when(session.get(Owner.class, 1L)).thenReturn(owner);

        Owner result = ownerDao.findById(1L);
        assertEquals(owner, result);
        verify(session).get(Owner.class, 1L);
        verify(session).close();
    }

    @Test
    void testFindById_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.findById(null));
    }

//    @Test
//    void testFindAll_ReturnsList() {
//        @SuppressWarnings("unchecked")
//        Query<Owner> q = (Query<Owner>) mock(Query.class);
//        List<Owner> mockList = List.of(new Owner(), new Owner());
//
//        when(session.createQuery("FROM Owner", Owner.class)).thenReturn(q);
//        when(q.list()).thenReturn(mockList);
//
//        List<Owner> result = ownerDao.findAll();
//        assertEquals(mockList, result);
//    }

    @Test
    void testFindByName_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Owner> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Owner> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);
        JpaPath<String> namePath = mock(JpaPath.class);
        JpaFunction<String> nameLower = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        Query<Owner> q = mock(Query.class);
        List<Owner> mockList = List.of(new Owner());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Owner.class)).thenReturn(cq);
        when(cq.from(Owner.class)).thenReturn(root);

        when(root.get("name")).thenReturn((JpaPath) namePath);
        when(cb.lower((Path) namePath)).thenReturn(nameLower);
        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(likePredicate);
        when(cq.where(likePredicate)).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(q);
        when(q.getResultList()).thenReturn(mockList);

        List<Owner> result = ownerDao.findByName("O");

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(q).getResultList();
    }

    @Test
    void testFindByName_NullName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.findByName(null));
    }

    @Test
    void testFindAll_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Owner> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Owner> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);
        Query<Owner> query = mock(Query.class);

        List<Owner> mockList = List.of(new Owner(), new Owner());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Owner.class)).thenReturn(cq);
        when(cq.from(Owner.class)).thenReturn(root);
        when(cq.select(root)).thenReturn(cq);
        when(session.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockList);

        List<Owner> result = ownerDao.findAll();

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(query).getResultList();
    }

    @Test
    void testFindByCatId_ReturnsOwner() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        when(session.getCriteriaBuilder()).thenReturn(cb);
        JpaCriteriaQuery<Owner> cq = mock(JpaCriteriaQuery.class);

        @SuppressWarnings("unchecked")
        JpaRoot<Owner> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);
        JpaJoin<Object, Object> catJoin = mock(JpaJoin.class);
        JpaPredicate equalPredicate = mock(JpaPredicate.class);
        Query<Owner> query = mock(Query.class);
        Owner owner = new Owner();


        when(cb.createQuery(Owner.class)).thenReturn(cq);
        when(cq.from(Owner.class)).thenReturn(root);

        when(root.join("cats")).thenReturn(catJoin);
        when(cb.equal(catJoin.get("id"), 1L)).thenReturn(equalPredicate);
        when(cq.where(equalPredicate)).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(owner));

        Owner result = ownerDao.findByCatId(1L);

        assertEquals(owner, result);
        verify(session).createQuery(cq);
        verify(query).getResultList();
    }

    @Test
    void testFindByCatId_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.findByCatId(null));
    }

    @Test
    void testFindByCatName_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Owner> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Owner> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);
        JpaJoin<Object, Object> catJoin = mock(JpaJoin.class);
        JpaPath<String> catNamePath = mock(JpaPath.class);
        JpaFunction<String> catLower = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        Query<Owner> q = mock(Query.class);
        List<Owner> mockList = List.of(new Owner());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Owner.class)).thenReturn(cq);
        when(cq.from(Owner.class)).thenReturn(root);

        when(root.join("cats")).thenReturn(catJoin);
        when(catJoin.get("name")).thenReturn((JpaPath) catNamePath);
        when(cb.lower((Path) catNamePath)).thenReturn(catLower);
        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(likePredicate);
        when(cq.where(likePredicate)).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(q);
        when(q.getResultList()).thenReturn(mockList);

        List<Owner> result = ownerDao.findByCatName("C");

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(q).getResultList();
    }

    @Test
    void testFindByCatName_NullName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ownerDao.findByCatName(null));
    }
}