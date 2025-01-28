package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PlannedActivityVm {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private List<PlannedActivityVm> plannedActivities;
    private LocalDateTime createdAt;
}
