package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    Plan findById(UUID id);

    List<Plan> findAll();

    Page<Plan> findAll(Pageable pageable);

    List<Plan> findUserPlans(UUID userId);

    Page<Plan> findUserPlans(Pageable pageable);

    @Transactional
    Plan create(PlanRequestDto planRequestDto);

    @Transactional
    Plan update(UUID id, PlanUpdateDto planUpdateDto);

    @Transactional
    void delete(UUID id);

    @Transactional
    void addActivityToPlan(UUID planId, UUID activityId, PlannedActivityRequestDto plannedActivityRequestDto);

    @Transactional
    void removeActivityFromPlan(UUID planId, UUID activityId);
}