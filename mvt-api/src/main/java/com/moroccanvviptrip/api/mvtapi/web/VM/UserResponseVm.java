package com.moroccanvviptrip.api.mvtapi.web.VM;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseVm {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private List<String> authorities;
}
