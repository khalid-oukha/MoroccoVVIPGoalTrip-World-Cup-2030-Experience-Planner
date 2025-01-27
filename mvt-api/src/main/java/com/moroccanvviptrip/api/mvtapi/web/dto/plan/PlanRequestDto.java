package com.moroccanvviptrip.api.mvtapi.web.dto.plan;


import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanRequestDto {

    @NotBlank
    @Trimmed
    private String name;

    @Trimmed
    private String description;

}
