package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponseVm {

    private UUID id;
    private String name;
    private String description;
    private String location;
    private boolean available;
    private LocalDateTime createdAt;

    private CategoryResponseVm category;
    private CityResponseVm city;
    private ArticleResponseVm article;
    private List<ActivityImageResponseVm> images;
}