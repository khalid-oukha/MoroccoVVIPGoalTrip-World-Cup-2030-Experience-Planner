package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;

import java.util.List;

public interface CityService {
    City create(CityRequestDto cityRequestDto);

    City update(Long id, CityUpdateDto cityUpdateDto);

    void delete(Long id);

    City getById(Long id);

    List<City> getAll();
}