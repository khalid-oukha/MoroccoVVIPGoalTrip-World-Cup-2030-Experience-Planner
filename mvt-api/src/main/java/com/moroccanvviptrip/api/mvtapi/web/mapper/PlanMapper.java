package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.web.VM.PlanResponseVm.PlanResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PlannedActivityMapper.class})
public interface PlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plannedActivities", ignore = true)
    @Mapping(target = "imageUrl", ignore = true) // Image is handled in service
    @Mapping(target = "createdAt", ignore = true)
    Plan toEntity(PlanRequestDto planRequestDto);

    PlanRequestDto toDto(Plan plan);

    @Mapping(target = "plannedActivities", source = "plannedActivities")
    PlanResponseVm toResponseVm(Plan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plannedActivities", ignore = true)
    @Mapping(target = "imageUrl", ignore = true) // Image is handled in service
    @Mapping(target = "createdAt", ignore = true)
    Plan partialUpdate(PlanUpdateDto planUpdateDto, @MappingTarget Plan plan);
}