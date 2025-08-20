package com.laba.impl;

import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.service.OwnerService;
import com.laba.validation.Validation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * The Implementation of OwnerService.
 */
@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerDao ownerDao;
    private final CatDao catDao;
    private final Validation validation;
    private final int maxOwnerNameLength;
    private final int maxCatNameLength;

    @Autowired
    public OwnerServiceImpl(OwnerDao ownerDao, CatDao catDao, Validation validation) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
        this.validation = validation;
        this.maxOwnerNameLength = validation.getOwner().getName();
        this.maxCatNameLength = validation.getCat().getName();
    }

    @Transactional
    @Override
    public OwnerDto getOwnerById(Long id) {
        if (id == null) throw new IllegalArgumentException("Owner id can't be null");
        Owner owner = ownerDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Hibernate.initialize(owner.getCats());
        return OwnerDto.fromEntity(owner);
    }

    @Transactional
    @Override
    public List<OwnerDto> getAllOwners() {
        List<Owner> owners = ownerDao.findAll();
        owners.forEach(owner -> Hibernate.initialize(owner.getCats()));
        return owners.stream()
                .map(OwnerDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public List<OwnerDto> findOwnersByName(String name) {
        validateString(name, "Owner name", maxOwnerNameLength);
        List<Owner> owners = ownerDao.findByNameIgnoreCase(name.trim());
        owners.forEach(owner -> Hibernate.initialize(owner.getCats()));
        return owners.stream()
                .map(OwnerDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public List<OwnerDto> findOwnersByCatName(String catName) {
        validateString(catName, "Cat name", maxCatNameLength);
        List<Owner> owners = ownerDao.findByCats_NameIgnoreCase(catName.trim());
        owners.forEach(owner -> Hibernate.initialize(owner.getCats()));
        return owners.stream()
                .map(OwnerDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public OwnerDto findOwnerByCatId(Long catId) {
        if (catId == null) throw new IllegalArgumentException("Cat id can't be null");
        Owner owner = ownerDao.findByCats_Id(catId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Hibernate.initialize(owner.getCats());
        return OwnerDto.fromEntity(owner);
    }


    @Transactional
    @Override
    public List<OwnerDto> findOwnersByFilter(OwnerDto filter) {
        if (filter == null) return getAllOwners();

        Specification<Owner> spec = Specification.where(null);

        if (filter.getId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("id"), filter.getId()));
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")),
                            "%" + filter.getName().toLowerCase().trim() + "%"));
        }

        if (filter.getBirthday() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("birthday"), filter.getBirthday()));
        }

        if (filter.getCatIds() != null && !filter.getCatIds().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.join("cats").get("id").in(filter.getCatIds()));
        }

        if (filter.getCatNames() != null && !filter.getCatNames().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(filter.getCatNames().stream()
                            .map(name -> cb.like(
                                    cb.lower(root.join("cats").get("name")),
                                    "%" + name.toLowerCase().trim() + "%"))
                            .toArray(jakarta.persistence.criteria.Predicate[]::new)));
        }

        List<Owner> owners = ownerDao.findAll(spec);
        owners.forEach(owner -> Hibernate.initialize(owner.getCats()));

        return owners.stream()
                .map(OwnerDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void saveOwner(OwnerDto ownerDto) {
        if (ownerDto == null) throw new IllegalArgumentException("Owner can't be null");
        Owner owner = ownerDto.toEntity();
        checkOwner(owner, true);
        ownerDao.save(owner);
    }

    @Override
    @Transactional
    public void updateOwner(OwnerDto ownerDto) {
        if (ownerDto == null) throw new IllegalArgumentException("Owner can't be null");
        Owner owner = ownerDto.toEntity();
        checkOwner(owner, false);
        ownerDao.save(owner);
    }

    @Override
    @Transactional
    public void deleteOwner(Long ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("Owner id can't be null");
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        if (owner.getCats() != null && !owner.getCats().isEmpty()) {
            throw new IllegalStateException("Can't delete owner with existing cats");
        }
        ownerDao.delete(owner);
    }

    @Transactional
    @Override
    public void addCatToOwner(Long ownerId, Long catId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Cat cat = catDao.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Cat not found"));

        if (cat.getOwner() != null && !cat.getOwner().getId().equals(ownerId)) {
            throw new IllegalStateException("Cat already has another owner");
        }

        cat.setOwner(owner);
        owner.getCats().add(cat);

        catDao.save(cat);
        ownerDao.save(owner);
    }

    @Transactional
    @Override
    public void removeCatFromOwner(Long ownerId, Long catId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Cat cat = catDao.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Cat not found"));

        if (!owner.getCats().contains(cat)) {
            throw new IllegalArgumentException("This cat does not belong to this owner");
        }

        owner.getCats().remove(cat);
        cat.setOwner(null);

        catDao.save(cat);
        ownerDao.save(owner);
    }


    /**
     * Checks Owner
     * @param owner owner to check
     * @param isNew boolean
     */
    private void checkOwner(Owner owner, boolean isNew) {
        if (owner == null) throw new IllegalArgumentException("Owner can't be null");
        if (!isNew && owner.getId() == null) throw new IllegalArgumentException("Owner id can't be null");
        validateString(owner.getName(), "Owner name", maxOwnerNameLength);
        if (owner.getBirthday() == null || owner.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid birthday");

    }

    /**
     * Validates input string
     * @param value string input
     * @param fieldName field name
     * @param maxLength maximum length of input string
     */
    private void validateString(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(fieldName + " can't be null or empty");
        if (value.length() > maxLength)
            throw new IllegalArgumentException(fieldName + " length must be ≤ " + maxLength);
    }

//    private void initializeOwner(Owner owner) {
//        if (owner.getCats() != null) {
//            owner.getCats().forEach(this::initializeCat);
//        }
//    }
//
//    private void initializeCat(Cat cat) {
//        if (cat.getOwner() != null) {
//            cat.getOwner().getId();
//            cat.getOwner().getName();
//        }
//        if (cat.getFriends() != null) {
//            cat.getFriends().size();
//        }
//    }
}