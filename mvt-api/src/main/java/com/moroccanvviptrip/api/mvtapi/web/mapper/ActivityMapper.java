package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.web.VM.ActivityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    Activity toEntity(ActivityRequestDto activityRequestDto);

    ActivityResponseVm toResponseVm(Activity activity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "activityUpdateDto.name")
    @Mapping(target = "description", source = "activityUpdateDto.description")
    @Mapping(target = "location", source = "activityUpdateDto.location")
    @Mapping(target = "available", source = "activityUpdateDto.available")
    @Mapping(target = "images", source = "activityUpdateDto.images")
    void partialUpdate(ActivityUpdateDto activityUpdateDto, @MappingTarget Activity existingActivity);
}
