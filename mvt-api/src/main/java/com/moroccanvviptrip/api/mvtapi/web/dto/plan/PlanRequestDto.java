package com.moroccanvviptrip.api.mvtapi.web.dto.plan;


import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlanRequestDto {

    @NotBlank
    @Trimmed
    private String name;

    @Trimmed
    private String description;

    private MultipartFile imageUri;

}
