package com.moroccanvviptrip.api.mvtapi.seeder;

import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.RoleRepository;
import com.moroccanvviptrip.api.mvtapi.repository.UserRepository;
import com.moroccanvviptrip.api.mvtapi.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class UserDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static String PASSWORD = "password";

    @Override
    public void run(String... args) {
        List<Role> roles = createRoles();
        createUsers(roles);
    }

    private List<Role> createRoles() {
        if (roleRepository.count() > 0)
            return roleRepository.findAll();

        return roleRepository.saveAll(List.of(
                Role.builder().name(AuthoritiesConstants.ROLE_USER).build(),
                Role.builder().name(AuthoritiesConstants.ROLE_ADMIN).build()
        ));
    }

    private void createUsers(List<Role> roles) {
        if (userRepository.count() > 0) return;

        roles.forEach(role -> {

            if (role.getName().equals(AuthoritiesConstants.ROLE_ADMIN)) {
                userRepository.save(User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Admin")
                        .lastName("User")
                        .roles(List.of(role))
                        .enabled(true)
                        .build());
            }

            if (role.getName().equals(AuthoritiesConstants.ROLE_USER)) {
                userRepository.save(User.builder()
                        .email("user@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("User")
                        .lastName("User")
                        .roles(List.of(role))
                        .enabled(true)
                        .build());
            }
        });
    }
}
