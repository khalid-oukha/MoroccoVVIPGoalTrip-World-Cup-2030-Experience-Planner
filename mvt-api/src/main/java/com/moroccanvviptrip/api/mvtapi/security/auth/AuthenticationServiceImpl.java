package com.moroccanvviptrip.api.mvtapi.security.auth;


import com.moroccanvviptrip.api.mvtapi.domain.Role;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.UserRepository;
import com.moroccanvviptrip.api.mvtapi.security.JwtService;
import com.moroccanvviptrip.api.mvtapi.security.TokenType;
import com.moroccanvviptrip.api.mvtapi.services.RoleService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request.SignInRequest;
import com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request.SignUpRequest;
import com.moroccanvviptrip.api.mvtapi.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

import static com.moroccanvviptrip.api.mvtapi.security.AuthoritiesConstants.ROLE_USER;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) throws ValidationException {
        Role roleUser = roleService.findByName(ROLE_USER)
                .orElseGet(() -> {
                    try {
                        return roleService.save(Role.builder().name(ROLE_USER).build());
                    } catch (ValidationException e) {
                        throw new RuntimeException(e);
                    }
                });

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(List.of(roleUser))
                .enabled(true)
                .build();

        userService.save(user);

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);

        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signing(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String refreshToken) throws ValidationException {
        if (jwtService.isTokenValid(refreshToken, TokenType.REFRESH_TOKEN)) {
            String username = jwtService.extractUserName(refreshToken);
            var user = userRepository.findByEmail(username).orElseThrow(() -> new ValidationException("email", "User not found"));
            var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
            return JwtAuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UnauthorizedException("Refresh token is invalid");
    }

    @Override
    public User me() {
        return userService.getCurrentUser();
    }
}
