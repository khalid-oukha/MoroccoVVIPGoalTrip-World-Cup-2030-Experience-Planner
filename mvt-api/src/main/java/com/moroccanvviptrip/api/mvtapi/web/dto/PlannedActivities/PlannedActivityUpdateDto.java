package com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities;

import com.moroccanvviptrip.api.mvtapi.domain.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
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

    @FutureOrPresent(message = "must be in future or present")
    private LocalDateTime startDate;

    @FutureOrPresent(message = "must be in future or present")
    private LocalDateTime endDate;
}
