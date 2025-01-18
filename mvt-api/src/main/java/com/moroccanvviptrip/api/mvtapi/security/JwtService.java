package com.moroccanvviptrip.api.mvtapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName (String token);

    String generateToken (UserDetails userDetails, TokenType tokenType);

    Authentication getAuthentication(String jwt);

    boolean isTokenValid (String token, UserDetails userDetails, TokenType tokenType);

    boolean isTokenValid (String token, TokenType tokenType);
}
