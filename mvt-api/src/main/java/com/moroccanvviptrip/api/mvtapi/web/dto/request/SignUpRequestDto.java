package com.moroccanvviptrip.api.mvtapi.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;


public record SignUpRequestDto(@NotBlank String firstName, @NotBlank String lastName, @Email @NotBlank String email,
                               @NotBlank String password) implements Serializable {
}