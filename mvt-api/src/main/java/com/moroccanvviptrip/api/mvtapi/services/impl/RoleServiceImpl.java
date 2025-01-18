package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.services.RoleService;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Role save(Role role) throws ValidationException {
        return null;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Role> getALlRoles() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public List<Role> findAllByNameIn(List<String> roleNames) {
        return List.of();
    }
}
