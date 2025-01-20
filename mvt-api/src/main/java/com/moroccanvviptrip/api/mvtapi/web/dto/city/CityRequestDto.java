package com.moroccanvviptrip.api.mvtapi.web.dto.city;


import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequestDto {
    @NotBlank
    @Unique(fieldName = "name", entityClass = City.class, message = "this city already exist")
    private String name;

    @NotBlank
    private String region;
}
