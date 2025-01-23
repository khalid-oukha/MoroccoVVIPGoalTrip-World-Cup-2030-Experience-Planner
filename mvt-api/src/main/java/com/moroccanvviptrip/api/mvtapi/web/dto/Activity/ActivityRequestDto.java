package com.moroccanvviptrip.api.mvtapi.web.dto.Activity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String location;
    
    private UUID categoryId;
    private UUID cityId;
    private List<MultipartFile> images;
}
