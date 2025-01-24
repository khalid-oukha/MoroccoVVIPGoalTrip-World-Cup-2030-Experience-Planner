package com.moroccanvviptrip.api.mvtapi.security.auth;


import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request.SignInRequest;
import com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request.SignUpRequest;

import javax.xml.bind.ValidationException;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request) throws ValidationException;

    JwtAuthenticationResponse signing(SignInRequest request);

    JwtAuthenticationResponse refreshToken(String refreshToken) throws ValidationException;

    User me();
}
