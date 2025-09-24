package com.laba.dao;

import com.laba.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.owner where u.username = :username")
    Optional<User> findByUsername(String username);
}