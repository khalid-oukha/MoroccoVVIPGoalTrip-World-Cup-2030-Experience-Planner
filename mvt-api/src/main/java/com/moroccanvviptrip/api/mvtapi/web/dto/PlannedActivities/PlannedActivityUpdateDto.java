package com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannedActivityUpdateDto {
    private Priority priority;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
