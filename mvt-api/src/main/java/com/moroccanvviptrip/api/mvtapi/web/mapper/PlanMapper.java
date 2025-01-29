package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    Plan toEntity(PlanRequestDto planRequestDto);

    PlanRequestDto toDto(Plan plan);

}
