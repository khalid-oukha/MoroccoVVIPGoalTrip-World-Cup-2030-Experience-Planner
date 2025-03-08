package com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlannedActivityRequestDto {

    @NotNull
    private Priority priority;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;
}