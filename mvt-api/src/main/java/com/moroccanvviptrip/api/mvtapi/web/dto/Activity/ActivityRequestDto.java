package com.moroccanvviptrip.api.mvtapi.web.dto.Activity;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequestDto {
    @NotBlank
    @Trimmed
    private String name;

    @NotBlank
    @Trimmed
    private String description;

    @NotBlank
    @Trimmed
    private String location;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long cityId;

    private MultipartFile imageUri;
}
