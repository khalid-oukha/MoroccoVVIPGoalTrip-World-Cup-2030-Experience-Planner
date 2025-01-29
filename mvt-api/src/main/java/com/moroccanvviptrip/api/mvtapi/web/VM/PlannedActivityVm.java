package com.moroccanvviptrip.api.mvtapi.web.VM;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PlannedActivityVm {
    private UUID id;
    private Priority priority;
    private ActivityResponseVm activity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
}
