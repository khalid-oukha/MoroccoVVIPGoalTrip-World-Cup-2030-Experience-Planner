package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.ActivityImage;
import com.moroccanvviptrip.api.mvtapi.web.dto.ActivItyImage.ActivityImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityImageMapper {

    ActivityImage toEntity(ActivityImageDto imageDto);

    String toResponse(ActivityImage activityImage);
}
