package com.moroccanvviptrip.api.mvtapi.web.dto.ActivItyImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityImageDto {
    private String imageUrl;
    private UUID activityId;
}
