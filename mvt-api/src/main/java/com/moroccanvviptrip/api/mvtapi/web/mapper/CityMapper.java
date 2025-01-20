package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.web.VM.CityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toCity(CityRequestDto cityRequestDto);

    CityResponseVm toResponse(City city);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    City partialUpdate(CityUpdateDto cityUpdateDto, @MappingTarget City existingCity);
}
