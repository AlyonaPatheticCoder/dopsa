package com.laba.impl;

import com.laba.dao.CatDao;
import com.laba.dto.CatDto;
import com.laba.entity.Cat;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.service.CatService;
import com.laba.validation.Validation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * CatService implementation
 */
@Service
public class CatServiceImpl implements CatService {

    private final CatDao catDao;
    private final Validation validation;
    private final int maxCatNameLength;
    private final int maxCatBreedLength;

    public CatServiceImpl(CatDao catDao, Validation validation) {
        this.catDao = catDao;
        this.validation = validation;
        this.maxCatNameLength = validation.getCat().getName();
        this.maxCatBreedLength = validation.getCat().getBreed();
    }

    @Override
    @Transactional(readOnly = true)
    public CatDto getCatById(Long id) {
        if (id == null) throw new IllegalArgumentException("Cat id can't be null");
        Cat cat = catDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Hibernate.initialize(cat.getOwner());
        Hibernate.initialize(cat.getFriends());
        return CatDto.fromEntity(cat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> getAllCats() {
        List<Cat> cats = catDao.findAll();
        cats.forEach(cat -> {
            Hibernate.initialize(cat.getOwner());
            Hibernate.initialize(cat.getFriends());
        });
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> findCatsByName(String name) {
        validateString(name, "Cat name", maxCatNameLength);
        List<Cat> cats = catDao.findByNameIgnoreCase(name.trim());
        cats.forEach(cat -> {
            Hibernate.initialize(cat.getOwner());
            Hibernate.initialize(cat.getFriends());
        });
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> findCatsByOwnerName(String ownerName) {
        validateString(ownerName, "Owner name", validation.getOwner().getName());
        List<Cat> cats = catDao.findByOwner_NameIgnoreCase(ownerName.trim());
        cats.forEach(cat -> {
            Hibernate.initialize(cat.getOwner());
            Hibernate.initialize(cat.getFriends());
        });
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> findCatsByOwnerId(Long ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("Owner id can't be null");
        List<Cat> cats = catDao.findByOwner_Id(ownerId);
        cats.forEach(cat -> {
            Hibernate.initialize(cat.getOwner());
            Hibernate.initialize(cat.getFriends());
        });
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> findCatsByFilter(CatDto filter) {
        Specification<Cat> spec = Specification.where(null);

        if (filter != null) {
            if (filter.getId() != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), filter.getId()));
            if (filter.getName() != null && !filter.getName().isBlank())
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            if (filter.getBreed() != null && !filter.getBreed().isBlank())
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("breed")), "%" + filter.getBreed().toLowerCase() + "%"));
            if (filter.getColor() != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("color"), filter.getColor()));
            if (filter.getBirthday() != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("birthday"), filter.getBirthday()));
            if (filter.getOwner() != null && filter.getOwner().getId() != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.join("owner").get("id"), filter.getOwner().getId()));
            if (filter.getFriends() != null && !filter.getFriends().isEmpty()) {
                List<Long> friendIds = filter.getFriends().stream().map(CatDto.FriendDto::getId).filter(Objects::nonNull).toList();
                if (!friendIds.isEmpty())
                    spec = spec.and((root, query, cb) -> root.join("friends").get("id").in(friendIds));
            }
        }

        List<Cat> cats = catDao.findAll(spec);
        cats.forEach(cat -> {
            Hibernate.initialize(cat.getOwner());
            Hibernate.initialize(cat.getFriends());
        });
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    @Transactional
    public CatDto saveCat(CatDto catDto) {
        if (catDto == null) throw new IllegalArgumentException("Cat can't be null");
        Cat cat = catDto.toEntity();
        checkCat(cat, true);
        Cat savedCat = catDao.save(cat);
        return CatDto.fromEntity(savedCat);
    }

    @Override
    @Transactional
    public CatDto updateCat(CatDto catDto) {
        if (catDto == null) throw new IllegalArgumentException("Cat can't be null");
        Cat cat = catDto.toEntity();
        checkCat(cat, false);
        Cat updatedCat = catDao.save(cat);
        return CatDto.fromEntity(updatedCat);
    }

    @Override
    @Transactional
    public void deleteCat(Long catId) {
        if (catId == null) throw new IllegalArgumentException("Cat id can't be null");
        Cat cat = catDao.findById(catId).orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        catDao.delete(cat);
    }

    @Override
    @Transactional
    public void addFriend(Long catId, Long friendId) {
        if (catId.equals(friendId)) throw new IllegalArgumentException("Cat cannot be friend with itself");
        Cat cat = catDao.findById(catId).orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Cat friend = catDao.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend cat not found"));
        cat.addFriend(friend);
        catDao.save(cat);
        catDao.save(friend);
    }

    @Override
    @Transactional
    public void removeFriend(Long catId, Long friendId) {
        Cat cat = catDao.findById(catId).orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Cat friend = catDao.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend cat not found"));
        cat.removeFriend(friend);
        catDao.save(cat);
        catDao.save(friend);
    }

    private void checkCat(Cat cat, boolean isNew) {
        if (cat == null) throw new IllegalArgumentException("Cat can't be null");
        if (!isNew && cat.getId() == null) throw new IllegalArgumentException("Cat id can't be null");
        validateString(cat.getName(), "Cat name", maxCatNameLength);
        if (cat.getBirthday() == null || cat.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid birthday");
        if (cat.getBreed() == null || cat.getBreed().isBlank())
            throw new IllegalArgumentException("Cat breed can't be blank");
        validateString(cat.getBreed(), "Cat breed", maxCatBreedLength);
        if (cat.getColor() == null) throw new IllegalArgumentException("Cat color can't be null");
    }

    private void validateString(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(fieldName + " can't be null or empty");
        if (value.length() > maxLength)
            throw new IllegalArgumentException(fieldName + " length must be ≤ " + maxLength);
    }
}
