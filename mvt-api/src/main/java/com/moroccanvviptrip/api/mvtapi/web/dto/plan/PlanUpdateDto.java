package com.moroccanvviptrip.api.mvtapi.web.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanUpdateDto {

    private String name;
    private String description;

    private MultipartFile imageUri;

}
