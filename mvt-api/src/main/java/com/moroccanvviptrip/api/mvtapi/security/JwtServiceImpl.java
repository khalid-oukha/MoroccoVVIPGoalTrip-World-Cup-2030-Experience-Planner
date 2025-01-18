package com.moroccanvviptrip.api.mvtapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService{
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Value("${token.validity.refresh.in.month}")
    private long refreshTokenValidityInMonth;

    @Value("${token.validity.access.in.minutes}")
    private long accessTokenValidityInMinutes;

    private static final String AUTHORITIES_KEY = "auth";

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        String authorities = authoritiesToCsv(userDetails.getAuthorities());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(AUTHORITIES_KEY, authorities);
        extraClaims.put("token_type", tokenType.name());
        return generateToken(extraClaims, userDetails, tokenType);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, TokenType tokenType) {
        long tokenExpirationInMin = tokenType == TokenType.ACCESS_TOKEN ?
                this.accessTokenValidityInMinutes :
                this.refreshTokenValidityInMonth * 30 * 24 * 60;
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationInMin * 60 * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        String username = extractUserName(token);
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(token);
        return isTokenNotExpired(token) &&
                checkTokenTypes(token, tokenType) &&
                username.equals(userDetails.getUsername()) &&
                isGrantedAuthoritiesEqual(authorities, userDetails.getAuthorities());
    }

    private boolean checkTokenTypes(String token, TokenType tokenType) {
        String typeToken = extractClaim(token, claims -> claims.get("token_type", String.class));
        return tokenType.name().equals(typeToken);
    }

    @Override
    public boolean isTokenValid(String token, TokenType tokenType) {
        return isTokenNotExpired(token) && checkTokenTypes(token, tokenType);
    }

    private boolean isTokenNotExpired(String token) {
        Date date = extractExpiration(token);
        if (date == null) return false;
        return date.after(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Authentication getAuthentication(String token) {
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(token);
        User principal = new User(extractUserName(token), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private boolean isGrantedAuthoritiesEqual(Collection<? extends GrantedAuthority> authorities1, Collection<? extends GrantedAuthority> authorities2) {
        return authoritiesToCsv(authorities1).equals(authoritiesToCsv(authorities2));
    }

    private String authoritiesToCsv(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
