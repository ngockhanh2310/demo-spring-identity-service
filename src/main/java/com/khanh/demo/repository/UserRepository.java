package com.khanh.demo.repository;

import com.khanh.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // check if user exists by username
    boolean existsByUsername(String username);

    // find user by username
    Optional<User> findByUsername(String username);
}
