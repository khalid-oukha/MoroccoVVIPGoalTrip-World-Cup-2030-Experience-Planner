package com.moroccanvviptrip.api.mvtapi.web.dto.city;

import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityUpdateDto {

    @Unique(fieldName = "name", entityClass = City.class, message = "this city already exist")
    private String name;

    private String region;
}
