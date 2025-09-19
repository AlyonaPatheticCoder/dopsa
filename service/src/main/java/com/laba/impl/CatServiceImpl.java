package com.laba.impl;

import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.dto.CatDto;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.service.CatService;
import com.laba.validation.Validation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * CatService implementation
 */
@Service
public class CatServiceImpl implements CatService {

    private final CatDao catDao;
    private final OwnerDao ownerDao;
    private final Validation validation;
    private final int maxOwnerNameLength;
    private final int maxCatNameLength;
    private final int maxCatBreedLength;

    @Autowired
    public CatServiceImpl(CatDao catDao, OwnerDao ownerDao, Validation validation) {
        this.catDao = catDao;
        this.ownerDao = ownerDao;
        this.validation = validation;
        this.maxOwnerNameLength = validation.getOwner().getName();
        this.maxCatNameLength = validation.getCat().getName();
        this.maxCatBreedLength = validation.getCat().getBreed();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User is not authenticated");
        }
        return ((UserDetailsImpl) auth.getPrincipal()).getUser();
    }

    private void checkOwnerAccess(Long ownerId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getOwner() == null || !currentUser.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("You don't have access to this owner's cats");
        }
    }

    @Transactional
    @Override
    public CatDto getCatById(Long id) {
        if (id == null) throw new IllegalArgumentException("Cat id can't be null");

        Cat cat = catDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Hibernate.initialize(cat.getOwner());

        checkOwnerAccess(cat.getOwner().getId());

        return CatDto.fromEntity(cat);
    }

    @Transactional
    @Override
    public List<CatDto> getAllCats() {
        User currentUser = getCurrentUser();
        List<Cat> cats;

        if (currentUser.getRole() == Role.ADMIN) {
            cats = catDao.findAll();
        } else {
            Long ownerId = currentUser.getOwner().getId();
            cats = catDao.findByOwner_Id(ownerId);
        }
        cats.forEach(cat -> Hibernate.initialize(cat.getOwner()));
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Transactional
    @Override
    public List<CatDto> findCatsByName(String name) {
        validateString(name, "Cat name", maxCatNameLength);
        List<Cat> cats = catDao.findByNameIgnoreCase(name.trim());

        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            Long ownerId = currentUser.getOwner().getId();
            cats = cats.stream()
                    .filter(cat -> cat.getOwner() != null && cat.getOwner().getId().equals(ownerId))
                    .toList();
        }

        cats.forEach(cat -> Hibernate.initialize(cat.getOwner()));
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Transactional
    @Override
    public List<CatDto> findCatsByOwnerName(String ownerName) {
        validateString(ownerName, "Owner name", maxOwnerNameLength);
        List<Cat> cats = catDao.findByOwner_NameIgnoreCase(ownerName.trim());

        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            Long ownerId = currentUser.getOwner().getId();
            cats = cats.stream()
                    .filter(cat -> cat.getOwner() != null && cat.getOwner().getId().equals(ownerId))
                    .toList();
        }

        cats.forEach(cat -> Hibernate.initialize(cat.getOwner()));
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Transactional
    @Override
    public List<CatDto> findCatsByOwnerId(Long ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("Owner id can't be null");
        checkOwnerAccess(ownerId);

        List<Cat> cats = catDao.findByOwner_Id(ownerId);
        cats.forEach(cat -> Hibernate.initialize(cat.getOwner()));
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Transactional
    @Override
    public List<CatDto> findCatsByFilter(CatDto filter) {
        User currentUser = getCurrentUser();

        Specification<Cat> spec = Specification.where(null);

        if (filter != null) {
            if (filter.getId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), filter.getId()));
            }
            if (filter.getName() != null && !filter.getName().isBlank()) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getBreed() != null && !filter.getBreed().isBlank()) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("breed")), "%" + filter.getBreed().toLowerCase() + "%"));
            }
            if (filter.getColor() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("color"), filter.getColor()));
            }
            if (filter.getBirthday() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("birthday"), filter.getBirthday()));
            }
            if (filter.getOwner() != null) {
                if (filter.getOwner().getId() != null) {
                    spec = spec.and((root, query, cb) -> cb.equal(root.join("owner").get("id"), filter.getOwner().getId()));
                }
                if (filter.getOwner().getName() != null && !filter.getOwner().getName().isBlank()) {
                    String ownerName = filter.getOwner().getName().toLowerCase();
                    spec = spec.and((root, query, cb) ->
                            cb.like(cb.lower(root.join("owner").get("name")), "%" + ownerName + "%"));
                }
            }
            if (filter.getFriends() != null && !filter.getFriends().isEmpty()) {
                List<Long> friendIds = filter.getFriends().stream()
                        .map(CatDto.FriendDto::getId)
                        .filter(Objects::nonNull)
                        .toList();
                if (!friendIds.isEmpty()) {
                    spec = spec.and((root, query, cb) -> root.join("friends").get("id").in(friendIds));
                }
            }
        }

        List<Cat> cats = catDao.findAll(spec);

        if (currentUser.getRole() != Role.ADMIN) {
            Long ownerId = currentUser.getOwner().getId();
            cats = cats.stream()
                    .filter(cat -> cat.getOwner() != null && cat.getOwner().getId().equals(ownerId))
                    .toList();
        }

        cats.forEach(cat -> Hibernate.initialize(cat.getOwner()));
        return cats.stream().map(CatDto::fromEntity).toList();
    }

    @Override
    public void saveCat(CatDto catDto) {
        if (catDto == null) throw new IllegalArgumentException("Cat can't be null");
        Cat cat = catDto.toEntity();
        checkCat(cat, true);
        checkOwnerAccess(cat.getOwner().getId());
        catDao.save(cat);
    }

    @Override
    public void updateCat(CatDto catDto) {
        if (catDto == null) throw new IllegalArgumentException("Cat can't be null");
        Cat cat = catDto.toEntity();
        checkCat(cat, false);
        checkOwnerAccess(cat.getOwner().getId());
        catDao.save(cat);
    }

    @Override
    public void deleteCat(Long catId) {
        if (catId == null) throw new IllegalArgumentException("Cat id can't be null");
        Cat cat = catDao.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        checkOwnerAccess(cat.getOwner().getId());
        catDao.delete(cat);
    }

    @Override
    public void addFriend(Long catId, Long friendId) {
        if (catId.equals(friendId)) throw new IllegalArgumentException("Cat cannot be friend with itself");

        Cat cat = catDao.findById(catId).orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Cat friend = catDao.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend cat not found"));

        checkOwnerAccess(cat.getOwner().getId());
        checkOwnerAccess(friend.getOwner().getId());

        if (cat.getFriends().contains(friend)) throw new IllegalStateException("Cats are already friends");

        cat.getFriends().add(friend);
        friend.getFriends().add(cat);

        catDao.save(cat);
        catDao.save(friend);
    }

    @Override
    public void removeFriend(Long catId, Long friendId) {
        Cat cat = catDao.findById(catId).orElseThrow(() -> new IllegalArgumentException("Cat not found"));
        Cat friend = catDao.findById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend cat not found"));

        checkOwnerAccess(cat.getOwner().getId());
        checkOwnerAccess(friend.getOwner().getId());

        if (!cat.getFriends().contains(friend)) {
            throw new IllegalArgumentException("Cats are not friends");
        }

        cat.getFriends().remove(friend);
        friend.getFriends().remove(cat);

        catDao.save(cat);
        catDao.save(friend);
    }

    private void checkCat(Cat cat, boolean isNew) {
        if (cat == null) throw new IllegalArgumentException("Cat can't be null");
        if (!isNew && cat.getId() == null) throw new IllegalArgumentException("Cat id can't be null");
        validateString(cat.getName(), "Cat name", maxCatNameLength);
        if (cat.getBirthday() == null || cat.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid birthday");
        if (cat.getColor() == null) throw new IllegalArgumentException("Cat color can't be null");
        if (cat.getOwner() == null || cat.getOwner().getId() == null)
            throw new IllegalArgumentException("Cat owner can't be null");
        if (!ownerDao.existsById(cat.getOwner().getId()))
            throw new IllegalArgumentException("Cat owner doesn't exist");
        validateString(cat.getBreed(), "Cat breed", maxCatBreedLength);
    }

    private void validateString(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(fieldName + " can't be null or empty");
        if (value.length() > maxLength)
            throw new IllegalArgumentException(fieldName + " length must be ≤ " + maxLength);
    }
}
