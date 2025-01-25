package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> findAll() {
        List<Role> roles = roleService.getALlRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @PostMapping
    public ResponseEntity<Role> save(@RequestBody Role role) throws ValidationException {
        Role savedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role role) throws ValidationException {
        Role updatedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
