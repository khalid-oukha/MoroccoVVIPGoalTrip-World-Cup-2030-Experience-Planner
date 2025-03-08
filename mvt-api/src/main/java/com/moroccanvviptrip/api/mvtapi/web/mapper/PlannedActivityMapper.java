package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.web.VM.PlannedActivityVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface PlannedActivityMapper {

    @Mapping(target = "activity", source = "activity")
    PlannedActivityVm toVm(PlannedActivity plannedActivity);

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "plan", ignore = true)
    PlannedActivity toEntity(PlannedActivityRequestDto requestDto);
}