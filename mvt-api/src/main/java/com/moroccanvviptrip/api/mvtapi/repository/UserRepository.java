package com.moroccanvviptrip.api.mvtapi.repository;

import com.moroccanvviptrip.api.mvtapi.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:query%" +
            " OR u.lastName LIKE %:query%" +
            " OR u.email LIKE %:query%")
    Page<User> searchByQuery(String query, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id = ? AND u.deletedAt IS NULL ")
    void softDelete(UUID id);

    @Query("SELECT u FROM User u WHERE u.id = ? AND u.deletedAt IS NOT NULL ")
    void  forceDelete(UUID id);
}
