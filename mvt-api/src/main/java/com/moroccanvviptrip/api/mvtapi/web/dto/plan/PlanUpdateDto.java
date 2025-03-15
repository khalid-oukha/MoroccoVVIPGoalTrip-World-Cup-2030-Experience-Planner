package com.moroccanvviptrip.api.mvtapi.web.dto.plan;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlanUpdateDto {

    private String name;
    private String description;

    private MultipartFile imageUri;

}
