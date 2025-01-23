package com.moroccanvviptrip.api.mvtapi.web.dto.Activity;

import com.moroccanvviptrip.api.mvtapi.web.dto.ActivItyImage.ActivityImageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateDto {
    private String name;
    private String description;
    private String location;
    private Boolean available;
    private UUID categoryId;
    private UUID cityId;
    private List<ActivityImageDto> images;
}
