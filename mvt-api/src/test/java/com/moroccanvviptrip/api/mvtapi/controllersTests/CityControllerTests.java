package com.moroccanvviptrip.api.mvtapi.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.city.CityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CityMapper;
import com.moroccanvviptrip.api.mvtapi.web.rest.CityController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CityControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CityService cityService;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityController cityController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cityController)
                .build();
    }

    @Test
    public void getAll_shouldReturnAllCities_whenNoRegionSpecified() throws Exception {
        // Arrange
        List<City> cities = Arrays.asList(
                new City(1L, "Marrakech", "Marrakech-Safi"),
                new City(2L, "Casablanca", "Casablanca-Settat")
        );

        List<CityResponseVm> cityResponses = Arrays.asList(
                CityResponseVm.builder().id(1L).name("Marrakech").region("Marrakech-Safi").build(),
                CityResponseVm.builder().id(2L).name("Casablanca").region("Casablanca-Settat").build()
        );

        when(cityService.findAll(null)).thenReturn(cities);
        when(cityMapper.toResponse(cities.get(0))).thenReturn(cityResponses.get(0));
        when(cityMapper.toResponse(cities.get(1))).thenReturn(cityResponses.get(1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/city"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Marrakech")))
                .andExpect(jsonPath("$[0].region", is("Marrakech-Safi")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Casablanca")))
                .andExpect(jsonPath("$[1].region", is("Casablanca-Settat")));

        verify(cityService).findAll(null);
    }

    @Test
    public void getAll_shouldReturnRegionFilteredCities_whenRegionSpecified() throws Exception {
        // Arrange
        String region = "Marrakech-Safi";

        List<City> cities = Arrays.asList(
                new City(1L, "Marrakech", "Marrakech-Safi"),
                new City(3L, "Essaouira", "Marrakech-Safi")
        );

        List<CityResponseVm> cityResponses = Arrays.asList(
                CityResponseVm.builder().id(1L).name("Marrakech").region("Marrakech-Safi").build(),
                CityResponseVm.builder().id(3L).name("Essaouira").region("Marrakech-Safi").build()
        );

        when(cityService.findAll(region)).thenReturn(cities);
        when(cityMapper.toResponse(cities.get(0))).thenReturn(cityResponses.get(0));
        when(cityMapper.toResponse(cities.get(1))).thenReturn(cityResponses.get(1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/city").param("region", region))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Marrakech")))
                .andExpect(jsonPath("$[0].region", is("Marrakech-Safi")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].name", is("Essaouira")))
                .andExpect(jsonPath("$[1].region", is("Marrakech-Safi")));

        verify(cityService).findAll(region);
    }

    @Test
    public void findById_shouldReturnCity_whenCityExists() throws Exception {
        // Arrange
        Long cityId = 1L;
        City city = new City(cityId, "Marrakech", "Marrakech-Safi");
        CityResponseVm cityResponse = CityResponseVm.builder()
                .id(cityId)
                .name("Marrakech")
                .region("Marrakech-Safi")
                .build();

        when(cityService.findById(cityId)).thenReturn(city);
        when(cityMapper.toResponse(city)).thenReturn(cityResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/city/{id}", cityId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marrakech")))
                .andExpect(jsonPath("$.region", is("Marrakech-Safi")));

        verify(cityService).findById(cityId);
    }


    @Test
    public void update_shouldUpdateAndReturnCity() throws Exception {
        // Arrange
        Long cityId = 1L;
        CityUpdateDto updateDto = new CityUpdateDto();
        updateDto.setRegion("Updated Region");

        City updatedCity = new City(cityId, "Marrakech", "Updated Region");
        CityResponseVm responseVm = CityResponseVm.builder()
                .id(cityId)
                .name("Marrakech")
                .region("Updated Region")
                .build();

        when(cityService.update(eq(cityId), any(CityUpdateDto.class))).thenReturn(updatedCity);
        when(cityMapper.toResponse(updatedCity)).thenReturn(responseVm);

        // Act & Assert
        mockMvc.perform(put("/api/v1/city/{id}", cityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marrakech")))
                .andExpect(jsonPath("$.region", is("Updated Region")));

        verify(cityService).update(eq(cityId), any(CityUpdateDto.class));
    }

    @Test
    public void delete_shouldReturnNoContent_whenCityExists() throws Exception {
        // Arrange
        Long cityId = 1L;
        doNothing().when(cityService).delete(cityId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/city/{id}", cityId))
                .andExpect(status().isNoContent());

        verify(cityService).delete(cityId);
    }

    @Test
    public void delete_shouldReturn404_whenCityDoesNotExist() throws Exception {
        // Arrange
        Long cityId = 999L;
        doThrow(new EntityNotFoundException("City not found!")).when(cityService).delete(cityId);

    }
}