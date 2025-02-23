package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.VM.ActivityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class ActivityMapper {

    @Autowired
    private FileStorageService fileStorageService;

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Activity toEntity(ActivityRequestDto activityRequestDto);

    @Mapping(source = "imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Activity UpdateDtotoEntity(ActivityUpdateDto activityUpdateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "activityUpdateDto.name", target = "name")
    @Mapping(source = "activityUpdateDto.description", target = "description")
    @Mapping(source = "activityUpdateDto.location", target = "location")
    @Mapping(source = "activityUpdateDto.available", target = "available")
    @Mapping(source = "activityUpdateDto.categoryId", target = "category.id")
    @Mapping(source = "activityUpdateDto.cityId", target = "city.id")
    @Mapping(source = "activityUpdateDto.imageUri", target = "imageUri", qualifiedByName = "mapImageUri")
    public abstract Activity partialUpdate(ActivityUpdateDto activityUpdateDto, Activity activity);

    public abstract ActivityResponseVm toResponseVm(Activity activity);

    @Named("mapImageUri")
    protected String mapImageUri(MultipartFile imageUri) {
        if (imageUri == null || imageUri.isEmpty()) {
            return null;
        }
        return fileStorageService.store(imageUri);
    }
}
