package com.moroccanvviptrip.api.mvtapi.web.dto.city;


import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRequestDto {
    @NotBlank(message = "name cannot be blank")
    @Unique(fieldName = "name", entityClass = City.class, message = "this city already exist")
    @Trimmed
    private String name;

    @NotBlank(message = "region cannot be blank")
    @Trimmed
    private String region;
}
