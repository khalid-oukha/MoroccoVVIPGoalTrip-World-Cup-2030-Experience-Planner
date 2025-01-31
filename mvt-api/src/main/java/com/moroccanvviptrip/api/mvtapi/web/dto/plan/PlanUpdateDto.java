package com.moroccanvviptrip.api.mvtapi.web.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanUpdateDto {

    private String name;
    private String description;
}
