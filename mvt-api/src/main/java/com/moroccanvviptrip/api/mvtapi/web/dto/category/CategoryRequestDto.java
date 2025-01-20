package com.moroccanvviptrip.api.mvtapi.web.dto.category;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    @NotBlank(message = "name cannot be blank")
    @Unique(fieldName = "name", entityClass = Category.class, message = "this category already exist")
    private String name;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @NotBlank(message = "imageUri cannot be blank")
    private String imageUri;
}
