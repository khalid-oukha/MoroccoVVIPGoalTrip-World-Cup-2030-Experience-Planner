package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityUpdateDto;

import java.util.List;
import java.util.UUID;

public interface PlannedActivityService {

    PlannedActivity findById(UUID id);

    List<PlannedActivity> findByPlanId(UUID planId);

    PlannedActivity update(UUID id, PlannedActivityUpdateDto updateDto);

    void delete(UUID id);
}