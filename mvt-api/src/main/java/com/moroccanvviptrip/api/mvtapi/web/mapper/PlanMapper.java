package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    Plan toEntity(PlanRequestDto planRequestDto);

    PlanRequestDto toDto(Plan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Plan partialUpdate(Plan plan, PlanRequestDto planRequestDto);
}
