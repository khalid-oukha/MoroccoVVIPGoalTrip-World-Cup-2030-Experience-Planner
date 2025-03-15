package com.moroccanvviptrip.api.mvtapi.web.rest;


import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CityMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @GetMapping
    public ResponseEntity<List<CityResponseVm>> getAll(
            @RequestParam(required = false) String region
    ) {
        List<City> cities = cityService.findAll(region);
        List<CityResponseVm> responseVmList = cities.stream().map(cityMapper::toResponse).toList();
        return ResponseEntity.ok(responseVmList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseVm> findById(@PathVariable Long id) {
        City city = cityService.findById(id);
        return ResponseEntity.ok(cityMapper.toResponse(city));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CityResponseVm> create(@Valid @RequestBody CityRequestDto cityRequestDto) {
        City createdCity = cityService.create(cityRequestDto);
        CityResponseVm responseVm = cityMapper.toResponse(createdCity);
        return ResponseEntity.created(URI.create("/api/v1/city/" + createdCity.getId())).body(responseVm);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CityResponseVm> update(@PathVariable Long id, @Valid @RequestBody CityUpdateDto cityUpdateDto) {
        City updatedCity = cityService.update(id, cityUpdateDto);
        CityResponseVm responseVm = cityMapper.toResponse(updatedCity);
        return ResponseEntity.ok(responseVm);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
