package com.userservice.user.repository;

import com.userservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User ,Long> {
    // This interface will automatically provide CRUD operations for User entity
    // You can add custom query methods here if needed

    Optional<User> findByUserEmail(String userEmail);
}
