package com.laba.impl;

import com.laba.DaoException;
import com.laba.entity.Cat;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for {@link CatDaoImpl} using Mockito.
 * <p>
 * Tests methods of CatDAO.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CatDaoImplTest {
    @Mock private SessionFactory sessionFactory;
    @Mock private Session session;
    @Mock private Transaction transaction;

    @InjectMocks
    private CatDaoImpl catDao;

    @BeforeEach
    void setUp() {
        // Dao.openSession() -> session
        lenient().when(sessionFactory.openSession()).thenReturn(session);
        lenient().when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    void testSave_CatSaved() {
        Cat cat = new Cat();
        assertDoesNotThrow(() -> catDao.save(cat));
        verify(session).persist(cat);
        verify(transaction).commit();
    }

    @Test
    void testSave_NullCat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.save(null));
    }

    @Test
    void testUpdate_CatUpdated() {
        Cat cat = new Cat();
        assertDoesNotThrow(() -> catDao.update(cat));
        verify(session).merge(cat);
        verify(transaction).commit();
    }

    @Test
    void testUpdate_NullCat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.update(null));
    }

    @Test
    void testDelete_CatDeleted() {
        Cat cat = new Cat();
        cat.setId(1L);
        when(session.get(Cat.class, 1L)).thenReturn(cat);

        assertDoesNotThrow(() -> catDao.delete(cat));
        verify(session).remove(cat);
        verify(transaction).commit();
    }

    @Test
    void testDelete_CatNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.delete(null));
    }

    @Test
    void testDelete_CatIdNull_ThrowsException() {
        Cat cat = new Cat();
        assertThrows(IllegalArgumentException.class, () -> catDao.delete(cat));
    }

    @Test
    void testDelete_CatNotFound_ThrowsDaoException() {
        Cat cat = new Cat();
        cat.setId(1L);
        when(session.get(Cat.class, 1L)).thenReturn(null);
        assertThrows(DaoException.class, () -> catDao.delete(cat));
    }


    @Test
    void testFindById_ReturnsCat() {
        Cat cat = new Cat();
        when(session.get(Cat.class, 1L)).thenReturn(cat);

        Cat result = catDao.findById(1L);
        assertEquals(cat, result);
    }

    @Test
    void testFindById_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.findById(null));
    }

    @Test
    void testFindAll_ReturnsList() {
        @SuppressWarnings("unchecked")
        Query<Cat> q = (Query<Cat>) mock(Query.class);
        List<Cat> mockList = List.of(new Cat(), new Cat());

        when(session.createQuery("FROM Cat", Cat.class)).thenReturn(q);
        when(q.list()).thenReturn(mockList);

        List<Cat> result = catDao.findAll();
        assertEquals(mockList, result);
    }


    @Test
    void testFindByName_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        when(session.getCriteriaBuilder()).thenReturn(cb);

        CriteriaQuery<Cat> cq = cb.createQuery(Cat.class);
        JpaRoot<Cat> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);

        @SuppressWarnings("unchecked")
        JpaPath<String> namePath = mock(JpaPath.class);
        when(root.get("name")).thenReturn((JpaPath) namePath);

        JpaFunction<String> nameLower = mock(JpaFunction.class);
        when(cb.lower((Path) namePath)).thenReturn(nameLower);

        JpaPredicate like = mock(JpaPredicate.class);
        //when(cb.like(nameLower, "C")).thenReturn(like);
        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(like);

        Query<Cat> q = mock(Query.class);
        List<Cat> mockList = List.of(new Cat());
        when(cq.from(Cat.class)).thenReturn(root);
        when(cq.where(like)).thenReturn(cq);
        when(session.createQuery(cq)).thenReturn(q);
        when(q.getResultList()).thenReturn(mockList);

        List<Cat> result = catDao.findByName("C");

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(q).getResultList();
    }

    @Test
    void testFindByName_NullName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.findByName(null));
    }


    @Test
    void testFindByOwnerName_NullName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.findByOwnerName(null));
    }


    @Test
    void testFindByOwnerId_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.findByOwnerId(null));
    }

    @Test
    void testFindByOwnerName_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Cat> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Cat> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);
        JpaJoin<Object, Object> ownerJoin = mock(JpaJoin.class);
        @SuppressWarnings("unchecked")
        JpaPath<String> ownerNamePath = mock(JpaPath.class);
        JpaFunction<String> ownerLower = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        Query<Cat> q = mock(Query.class);

        List<Cat> mockList = List.of(new Cat());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Cat.class)).thenReturn(cq);
        when(cq.from(Cat.class)).thenReturn(root);

        when(root.join("owner")).thenReturn(ownerJoin);
        when(ownerJoin.get("name")).thenReturn((JpaPath) ownerNamePath);

        when(cb.lower((Path) ownerNamePath)).thenReturn(ownerLower);
        //when(cb.like(ownerLower, "O")).thenReturn(likePredicate);
        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(likePredicate);

        when(cq.where(likePredicate)).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(q);
        when(q.getResultList()).thenReturn(mockList);

        List<Cat> result = catDao.findByOwnerName("O");

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(q).getResultList();
    }

    @Test
    void testFindByOwnerId_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Cat> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Cat> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);

        Query<Cat> q = mock(Query.class);
        List<Cat> mockList = List.of(new Cat());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Cat.class)).thenReturn(cq);
        when(cq.from(Cat.class)).thenReturn(root);

        when(cb.equal(root.join("owner").get("id"), 1L)).thenReturn(mock(JpaPredicate.class));
        when(cq.where(any(JpaPredicate.class))).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(q);
        when(q.getResultList()).thenReturn(mockList);

        List<Cat> result = catDao.findByOwnerId(1L);

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(q).getResultList();
    }

    @Test
    void testFindByNameAndOwner_ReturnsList() {
        HibernateCriteriaBuilder cb = mock(HibernateCriteriaBuilder.class, RETURNS_DEEP_STUBS);
        JpaCriteriaQuery<Cat> cq = mock(JpaCriteriaQuery.class);
        JpaRoot<Cat> root = mock(JpaRoot.class, RETURNS_DEEP_STUBS);

        Query<Cat> query = mock(Query.class);
        List<Cat> mockList = List.of(new Cat());

        when(session.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Cat.class)).thenReturn(cq);
        when(cq.from(Cat.class)).thenReturn(root);

        JpaFunction<String> nameLower = mock(JpaFunction.class);
        JpaPredicate nameLike = mock(JpaPredicate.class);
        when(cb.lower(root.get("name"))).thenReturn(nameLower);
        //when(cb.like(nameLower, "C")).thenReturn(nameLike);

        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(nameLike);

        JpaFunction<String> ownerLower = mock(JpaFunction.class);
        JpaPredicate ownerLike = mock(JpaPredicate.class);
        when(cb.lower(root.join("owner").get("name"))).thenReturn(ownerLower);
        //when(cb.like(ownerLower, "O")).thenReturn(ownerLike);
        when(cb.like(any(JpaFunction.class), anyString())).thenReturn(ownerLike);


        JpaPredicate combined = mock(JpaPredicate.class);
        //when(cb.and(nameLike, ownerLike)).thenReturn(combined);
        when(cb.and(any(JpaPredicate.class), any(JpaPredicate.class))).thenReturn(combined);
        when(cq.where(combined)).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockList);

        List<Cat> result = catDao.findByNameAndOwner("C", "O");

        assertEquals(mockList, result);
        verify(session).createQuery(cq);
        verify(query).getResultList();
    }

    @Test
    void testFindByNameAndOwner_NullArguments_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> catDao.findByNameAndOwner(null, "O"));
        assertThrows(IllegalArgumentException.class, () -> catDao.findByNameAndOwner("C", null));
    }
}