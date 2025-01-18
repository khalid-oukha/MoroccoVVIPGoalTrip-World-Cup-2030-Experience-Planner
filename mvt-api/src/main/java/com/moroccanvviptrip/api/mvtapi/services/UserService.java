package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User save(User user);

    User findById(UUID id);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable, String query);

    List<String> getAuthorities();

    UserDetailsService userDetailsService();

    User findByUsername(String username);

    List<String> getMyAuthorities();

    User getCurrentUser();

    void delete(UUID id);

    void softDelete(UUID id);

    void forceDelete(UUID id);

    void enable(UUID id, boolean enable);

    void handleRoles(UUID id, List<String> roles) throws ValidationException, ResourceNotFoundException;
}
