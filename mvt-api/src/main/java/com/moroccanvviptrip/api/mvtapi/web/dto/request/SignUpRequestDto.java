package com.moroccanvviptrip.api.mvtapi.web.dto.request;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;


public record SignUpRequestDto(

        @NotBlank
        @Trimmed
        String firstName,

        @NotBlank
        @Trimmed
        String lastName,

        @Email
        @NotBlank
        @Trimmed
        String email,

        @NotBlank
        @Trimmed
        String password)

        implements Serializable {
}