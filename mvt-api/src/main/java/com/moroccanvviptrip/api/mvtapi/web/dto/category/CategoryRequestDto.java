package com.moroccanvviptrip.api.mvtapi.web.dto.category;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDto {

    @NotBlank
    @Trimmed
    @Unique(fieldName = "name", entityClass = Category.class, message = "this category already exist")
    private String name;

    @NotBlank
    @Trimmed
    private String description;

    private MultipartFile imageUri;
}
