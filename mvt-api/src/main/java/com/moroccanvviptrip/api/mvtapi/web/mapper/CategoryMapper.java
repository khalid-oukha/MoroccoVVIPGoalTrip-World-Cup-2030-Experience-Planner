package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CategoryResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Autowired
    private FileStorageService fileStorageService;

    private final String baseFileUrl = "http://localhost:8080/api/v1/files/";

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Category toEntity(CategoryRequestDto categoryRequestDto);

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Category UpdateDtotoEntity(CategoryUpdateDto categoryUpdateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "categoryUpdateDto.name", target = "name")
    @Mapping(source = "categoryUpdateDto.description", target = "description")
    @Mapping(source = "categoryUpdateDto.imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Category partialUpdate(CategoryUpdateDto categoryUpdateDto, @MappingTarget Category category);

    public abstract CategoryResponseVm toResponse(Category category);

    @Named("mapImageUri")
    protected String mapImageUri(MultipartFile imageUri) {
        if (imageUri == null || imageUri.isEmpty()) {
            return null;
        }

        String filename = fileStorageService.store(imageUri);

        return baseFileUrl + filename;
    }
}