package com.moroccanvviptrip.api.mvtapi.web.dto.Activity;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateDto {
    @Trimmed
    private String name;
    @Trimmed
    private String description;
    @Trimmed
    private String location;
    private Boolean available;
    private Long categoryId;
    private Long cityId;
    private MultipartFile imageUri;
}
