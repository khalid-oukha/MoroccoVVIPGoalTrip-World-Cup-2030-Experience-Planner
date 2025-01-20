package com.moroccanvviptrip.api.mvtapi.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CityDataSeeder implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final Resource citiesJsonResource;

    public CityDataSeeder(CityRepository cityRepository, @Value("classpath:/db/data/cities.json") Resource citiesJsonResource) {
        this.cityRepository = cityRepository;
        this.citiesJsonResource = citiesJsonResource;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cityRepository.count() > 0) {
            log.info("Cities are already loaded into the database.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<City> cities = objectMapper.readValue(citiesJsonResource.getInputStream(), new TypeReference<List<City>>() {});
            log.info("Cities data loaded: {} cities found.", cities.size());
            cityRepository.saveAll(cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
