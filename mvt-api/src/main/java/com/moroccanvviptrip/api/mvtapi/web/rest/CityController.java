package com.moroccanvviptrip.api.mvtapi.web.rest;


import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @GetMapping
    public ResponseEntity<List<CityResponseVm>> getAll() {
        List<City> cities = cityService.getAll();
        List<CityResponseVm> responseVmList = cities.stream().map(cityMapper::toResponse).toList();
        return ResponseEntity.ok(responseVmList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseVm> getById(@PathVariable Long id) {
        City city = cityService.getById(id);
        return ResponseEntity.ok(cityMapper.toResponse(city));
    }


}
