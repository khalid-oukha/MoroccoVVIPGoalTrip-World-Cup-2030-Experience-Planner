package com.moroccanvviptrip.api.mvtapi.web.dto.Auth.request;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "First name cannot be blank")
    @Trimmed
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Trimmed
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Trimmed
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Trimmed
    private String password;

    private List<String> authorities;
}
