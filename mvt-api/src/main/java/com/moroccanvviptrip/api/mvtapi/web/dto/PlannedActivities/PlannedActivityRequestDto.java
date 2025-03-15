package com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannedActivityRequestDto {

    @NotNull
    private Priority priority;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;
}