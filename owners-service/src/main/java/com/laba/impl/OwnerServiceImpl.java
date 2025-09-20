package com.laba.impl;
import com.laba.dao.OwnerDao;
import com.laba.dto.CatDto;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.service.OwnerService;
import com.laba.validation.Validation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * The Implementation of OwnerService.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDao ownerDao;
    private final Validation validation;
    private final int maxOwnerNameLength;

    @Autowired
    public OwnerServiceImpl(OwnerDao ownerDao, Validation validation) {
        this.ownerDao = ownerDao;
        this.validation = validation;
        this.maxOwnerNameLength = validation.getOwner().getName();
    }

    @Override
    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerDao.findByIdWithCats(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return OwnerDto.fromEntity(owner);
    }

    @Override
    public List<OwnerDto> getAllOwners() {
        List<Owner> owners = ownerDao.findAllWithCats();
        return owners.stream().map(OwnerDto::fromEntity).toList();
    }

    @Override
    public List<OwnerDto> findOwnersByFilter(OwnerDto filter) {
        Specification<Owner> spec = Specification.where(null);

        if (filter != null) {
            if (filter.getId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), filter.getId()));
            }
            if (filter.getName() != null && !filter.getName().isBlank()) {
                String name = filter.getName().trim().toLowerCase();
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("name")), "%" + name + "%"));
            }
            if (filter.getBirthday() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("birthday"), filter.getBirthday()));
            }
        }

        List<Owner> owners = ownerDao.findAll(spec);
        return owners.stream().map(OwnerDto::fromEntity).toList();
    }

    @Override
    public List<OwnerDto> findOwnersByName(String name) {
        validateString(name, "Owner name", maxOwnerNameLength);
        List<Owner> owners = ownerDao.findByNameIgnoreCase(name.trim());
        return owners.stream().map(OwnerDto::fromEntity).toList();
    }

    @Override
    public OwnerDto findOwnerByCatId(Long catId) {
        if (catId == null) throw new IllegalArgumentException("Cat id can't be null");
        Owner owner = ownerDao.findByCats_Id(catId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return OwnerDto.fromEntity(owner);
    }

    @Override
    public void saveOwner(OwnerDto ownerDto) {
        if (ownerDto == null) throw new IllegalArgumentException("Owner can't be null");
        Owner owner = ownerDto.toEntity();
        checkOwner(owner, true);
        ownerDao.save(owner);
    }

    @Override
    public void updateOwner(OwnerDto ownerDto) {
        if (ownerDto == null) throw new IllegalArgumentException("Owner can't be null");
        Owner owner = ownerDto.toEntity();
        checkOwner(owner, false);
        ownerDao.save(owner);
    }

    @Override
    public void deleteOwner(Long ownerId) {
        Owner owner = ownerDao.findByIdWithCats(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        if (!owner.getCats().isEmpty()) {
            throw new IllegalStateException("Can't delete owner with existing cats");
        }
        ownerDao.delete(owner);
    }

    @Transactional
    @Override
    public void addCatToOwner(Long ownerId, Long catId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        if (owner.getCats().stream().noneMatch(c -> c.getId().equals(catId))) {
            Cat dummyCat = new Cat();
            dummyCat.setId(catId);
            owner.getCats().add(dummyCat);
            ownerDao.save(owner);
        }
    }

    @Transactional
    @Override
    public void removeCatFromOwner(Long ownerId, Long catId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        owner.getCats().removeIf(c -> c.getId().equals(catId));
        ownerDao.save(owner);
    }

    private void checkOwner(Owner owner, boolean isNew) {
        if (owner == null) throw new IllegalArgumentException("Owner can't be null");
        if (!isNew && owner.getId() == null) throw new IllegalArgumentException("Owner id can't be null");
        validateString(owner.getName(), "Owner name", maxOwnerNameLength);
        if (owner.getBirthday() == null || owner.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid birthday");
    }

    private void validateString(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(fieldName + " can't be null or empty");
        if (value.length() > maxLength)
            throw new IllegalArgumentException(fieldName + " length must be ≤ " + maxLength);
    }
}