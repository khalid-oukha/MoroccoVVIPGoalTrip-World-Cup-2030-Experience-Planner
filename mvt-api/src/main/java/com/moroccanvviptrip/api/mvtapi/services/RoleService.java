package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Role;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role save(Role role) throws ValidationException;

    Optional<Role> findByName(String name);

    List<Role> getALlRoles();

    void delete(Long id);

    Role findById(Long id);

    List<Role> findAllByNameIn(List<String> roleNames);
}
