package com.moroccanvviptrip.api.mvtapi.web.dto.category;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    @Unique(fieldName = "name", entityClass = Category.class, message = "this category already exist")
    private String name;

    private String description;

    private MultipartFile imageUri;
}
