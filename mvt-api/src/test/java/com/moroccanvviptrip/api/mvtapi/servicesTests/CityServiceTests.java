package com.moroccanvviptrip.api.mvtapi.servicesTests;

import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.repository.CityRepository;
import com.moroccanvviptrip.api.mvtapi.services.impl.CityServiceImpl;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.exception.PropertyExistsException;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CityMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CityServiceTests {
    private CityRepository cityRepository;
    private CityMapper cityMapper;
    private CityServiceImpl cityService;

    @BeforeEach
    public void beforeEach() {
        cityRepository = mock(CityRepository.class);
        cityMapper = mock(CityMapper.class);
        cityService = new CityServiceImpl(cityRepository, cityMapper);
    }

    @Test
    public void create_shouldCreateCity_whenValidRequest() {
        CityRequestDto requestDto = new CityRequestDto();
        requestDto.setName("Marrakech");
        requestDto.setRegion("Marrakech-Safi");

        City cityEntity = new City();
        cityEntity.setName("Marrakech");
        cityEntity.setRegion("Marrakech-Safi");

        City savedCity = new City();
        savedCity.setId(1L);
        savedCity.setName("Marrakech");
        savedCity.setRegion("Marrakech-Safi");

        when(cityRepository.existsByName("Marrakech")).thenReturn(false);
        when(cityMapper.toCity(requestDto)).thenReturn(cityEntity);
        when(cityRepository.save(cityEntity)).thenReturn(savedCity);

        // Act
        City result = cityService.create(requestDto);

        // Assert
        assertEquals(savedCity, result);
        verify(cityRepository, times(1)).existsByName("Marrakech");
        verify(cityMapper, times(1)).toCity(requestDto);
        verify(cityRepository, times(1)).save(cityEntity);
    }

    @Test
    public void create_shouldThrowPropertyExistsException_whenCityNameExists() {
        // Arrange
        CityRequestDto requestDto = new CityRequestDto();
        requestDto.setName("Marrakech");
        requestDto.setRegion("Marrakech-Safi");

        when(cityRepository.existsByName("Marrakech")).thenReturn(true);

        // Act & Assert
        PropertyExistsException exception = assertThrows(PropertyExistsException.class,
                () -> cityService.create(requestDto),
                "Should throw PropertyExistsException when city name already exists"
        );
        assertEquals("this city already exist", exception.getMessage());
        verify(cityRepository, times(1)).existsByName("Marrakech");
        verify(cityMapper, never()).toCity(any(CityRequestDto.class));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    public void update_shouldUpdateCity_whenCityExists() {
        // Arrange
        Long cityId = 1L;
        CityUpdateDto updateDto = new CityUpdateDto();
        updateDto.setRegion("Updated Region");

        City existingCity = new City();
        existingCity.setId(cityId);
        existingCity.setName("Marrakech");
        existingCity.setRegion("Marrakech-Safi");

        City updatedCityEntity = new City();
        updatedCityEntity.setId(cityId);
        updatedCityEntity.setName("Marrakech");
        updatedCityEntity.setRegion("Updated Region");

        City savedCity = new City();
        savedCity.setId(cityId);
        savedCity.setName("Marrakech");
        savedCity.setRegion("Updated Region");

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
        when(cityMapper.partialUpdate(updateDto, existingCity)).thenReturn(updatedCityEntity);
        when(cityRepository.save(updatedCityEntity)).thenReturn(savedCity);

        // Act
        City result = cityService.update(cityId, updateDto);

        // Assert
        assertEquals(savedCity, result);
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityMapper, times(1)).partialUpdate(updateDto, existingCity);
        verify(cityRepository, times(1)).save(updatedCityEntity);
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenCityDoesNotExist() {
        // Arrange
        Long cityId = 999L;
        CityUpdateDto updateDto = new CityUpdateDto();
        updateDto.setRegion("Updated Region");

        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cityService.update(cityId, updateDto),
                "Should throw EntityNotFoundException when city does not exist"
        );
        assertEquals("City not found", exception.getMessage());
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityMapper, never()).partialUpdate(any(CityUpdateDto.class), any(City.class));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    public void delete_shouldDeleteCity_whenCityExists() {
        // Arrange
        Long cityId = 1L;
        City existingCity = new City();
        existingCity.setId(cityId);
        existingCity.setName("Marrakech");
        existingCity.setRegion("Marrakech-Safi");

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));

        // Act
        cityService.delete(cityId);

        // Assert
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).delete(existingCity);
    }

    @Test
    public void delete_shouldThrowEntityNotFoundException_whenCityDoesNotExist() {
        // Arrange
        Long cityId = 999L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cityService.delete(cityId),
                "Should throw EntityNotFoundException when city does not exist"
        );
        assertEquals("city not found !", exception.getMessage());
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, never()).delete(any(City.class));
    }

    @Test
    public void findById_shouldReturnCity_whenCityExists() {
        // Arrange
        Long cityId = 1L;
        City existingCity = new City();
        existingCity.setId(cityId);
        existingCity.setName("Marrakech");
        existingCity.setRegion("Marrakech-Safi");

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));

        // Act
        City result = cityService.findById(cityId);

        // Assert
        assertEquals(existingCity, result);
        verify(cityRepository, times(1)).findById(cityId);
    }

    @Test
    public void findById_shouldThrowEntityNotFoundException_whenCityDoesNotExist() {
        // Arrange
        Long cityId = 999L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cityService.findById(cityId),
                "Should throw EntityNotFoundException when city does not exist"
        );
        assertEquals("City not found !", exception.getMessage());
        verify(cityRepository, times(1)).findById(cityId);
    }

    @Test
    public void findAll_shouldReturnAllCities_whenRegionIsNull() {
        // Arrange
        List<City> expectedCities = Arrays.asList(
                createCity(1L, "Marrakech", "Marrakech-Safi"),
                createCity(2L, "Casablanca", "Casablanca-Settat"),
                createCity(3L, "Rabat", "Rabat-Salé-Kénitra")
        );

        when(cityRepository.findAll()).thenReturn(expectedCities);

        // Act
        List<City> result = cityService.findAll(null);

        // Assert
        assertEquals(expectedCities.size(), result.size());
        assertEquals(expectedCities, result);
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, never()).findByRegion(anyString());
    }

    @Test
    public void findAll_shouldReturnAllCities_whenRegionIsEmpty() {
        // Arrange
        List<City> expectedCities = Arrays.asList(
                createCity(1L, "Marrakech", "Marrakech-Safi"),
                createCity(2L, "Casablanca", "Casablanca-Settat"),
                createCity(3L, "Rabat", "Rabat-Salé-Kénitra")
        );

        when(cityRepository.findAll()).thenReturn(expectedCities);

        // Act
        List<City> result = cityService.findAll("");

        // Assert
        assertEquals(expectedCities.size(), result.size());
        assertEquals(expectedCities, result);
        verify(cityRepository, times(1)).findAll();
        verify(cityRepository, never()).findByRegion(anyString());
    }

    @Test
    public void findAll_shouldReturnCitiesByRegion_whenRegionIsProvided() {
        // Arrange
        String region = "Marrakech-Safi";
        List<City> expectedCities = Arrays.asList(
                createCity(1L, "Marrakech", region),
                createCity(4L, "Essaouira", region)
        );

        when(cityRepository.findByRegion(region)).thenReturn(expectedCities);

        // Act
        List<City> result = cityService.findAll(region);

        // Assert
        assertEquals(expectedCities.size(), result.size());
        assertEquals(expectedCities, result);
        verify(cityRepository, times(1)).findByRegion(region);
        verify(cityRepository, never()).findAll();
    }

    // Helper method to create City objects for testing
    private City createCity(Long id, String name, String region) {
        City city = new City();
        city.setId(id);
        city.setName(name);
        city.setRegion(region);
        return city;
    }
}