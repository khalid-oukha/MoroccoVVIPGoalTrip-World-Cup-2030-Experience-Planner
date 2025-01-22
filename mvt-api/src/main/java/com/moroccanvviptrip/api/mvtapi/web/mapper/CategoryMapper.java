package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CategoryResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Autowired
    private FileStorageService fileStorageService;

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Category toEntity(CategoryRequestDto categoryRequestDto);

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Category UpdateDtotoEntity(CategoryUpdateDto categoryUpdateDto);

    public abstract CategoryResponseVm toResponse(Category category);

    @Named("mapImageUri")
    protected String mapImageUri(MultipartFile imageUri) {
        if (imageUri == null || imageUri.isEmpty()) {
            return null;
        }
        return fileStorageService.store(imageUri);
    }
}