package com.oide.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oide.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
