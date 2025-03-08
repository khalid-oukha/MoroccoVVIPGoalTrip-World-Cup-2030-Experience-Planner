package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.web.VM.PlanResponseVm.PlanResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PlannedActivityMapper.class})
public interface PlanMapper {

    Plan toEntity(PlanRequestDto planRequestDto);

    PlanRequestDto toDto(Plan plan);

    @Mapping(target = "plannedActivities", source = "plannedActivities")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    PlanResponseVm toResponseVm(Plan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Plan partialUpdate(PlanUpdateDto planUpdateDto, @MappingTarget Plan plan);
}