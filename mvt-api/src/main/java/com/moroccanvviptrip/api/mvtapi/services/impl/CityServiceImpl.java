package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.repository.CityRepository;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.exception.PropertyExistsException;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public City create(CityRequestDto cityRequestDto) {
        if (cityRepository.existsByName(cityRequestDto.getName())) {
            throw new PropertyExistsException("this city already exist");
        }
        return cityRepository.save(cityMapper.toCity(cityRequestDto));
    }

    @Override
    public City update(Long id, CityUpdateDto cityUpdateDto) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        City city = cityMapper.partialUpdate(cityUpdateDto, existingCity);
        return cityRepository.save(city);
    }

    @Override
    public void delete(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("city not found !"));

        cityRepository.delete(city);
    }

    @Override
    public City getById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found !"));
    }

    @Override
    public List<City> getAll(String region) {
        if (region != null && !region.isEmpty()) {
            return cityRepository.findByRegion(region);
        } else {
            return cityRepository.findAll();
        }
    }
    
}
