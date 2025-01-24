package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.repository.RoleRepository;
import com.moroccanvviptrip.api.mvtapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) throws ValidationException {
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());
        if (optionalRole.isPresent())
            throw new ValidationException("name", "Role with this name already exists");
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> getALlRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent())
            roleRepository.delete(role.get());
        else
            throw new NoSuchElementException("Role not found with id: " + id);
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isEmpty())
            throw new IllegalArgumentException("role doesn't exist with this id: " + id);
        return roleOptional.get();
    }

    @Override
    public List<Role> findAllByNameIn(List<String> roleNames) {
        return roleRepository.findAllByNameIn(roleNames);
    }
}
