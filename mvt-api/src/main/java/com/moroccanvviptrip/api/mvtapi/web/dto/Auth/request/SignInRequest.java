package com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @NotBlank
    @Email
    @Trimmed
    private String email;
    @NotBlank
    @Trimmed
    private String password;
}
