package com.moroccanvviptrip.api.mvtapi.services.impl;


import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.UserRepository;
import com.moroccanvviptrip.api.mvtapi.security.utils.SecurityUtils;
import com.moroccanvviptrip.api.mvtapi.services.RoleService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.web.exception.EmailAlreadyExistException;
import com.moroccanvviptrip.api.mvtapi.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public User save(User user) {
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException();
        }

        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable, String query) {
        if (query != null && !query.isEmpty())
            return userRepository.searchByQuery(query, pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public List<String> getAuthorities() {
        return roleService.getALlRoles().stream().map(Role::getName).toList();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<String> getMyAuthorities() {
        return getCurrentUser()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public User getCurrentUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserEmail();
        if(currentUserLogin == null)
            throw new BadCredentialsException("USER_NOT_FOUND");
        return this.findByUsername(currentUserLogin);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void softDelete(UUID id) {
        userRepository.softDelete(id);
    }

    @Override
    public void forceDelete(UUID id) {
        userRepository.forceDelete(id);
    }

    @Override
    public void enable(UUID id, boolean enable) {

    }

    @Override
    public void handleRoles(UUID id, List<String> roles) throws ValidationException, ResourceNotFoundException {
        if (roles.isEmpty()) {
            throw new ValidationException("Roles cannot be empty");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "User not found"));

        List<Role> rolesList = roleService.findAllByNameIn(roles);

        if (rolesList.size() > roles.size()) {
            List<String> notFoundRoles = roles.stream()
                    .filter(role -> rolesList.stream().noneMatch(r -> r.getName().equals(role)))
                    .toList();

            throw new ResourceNotFoundException("role", "Roles not found: " + String.join(", ", notFoundRoles));
        }

        if (new HashSet<>(user.getRoles()).containsAll(rolesList) && rolesList.containsAll(user.getRoles())) {
            throw new ValidationException("User already has the same roles");
        }

        user.getRoles().clear();
        user.getRoles().addAll(rolesList);
        userRepository.save(user);
    }
}
