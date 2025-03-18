package com.moroccanvviptrip.api.mvtapi.repository;

import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlannedActivityRepository extends JpaRepository<PlannedActivity, UUID> {
    List<PlannedActivity> findByPlanId(UUID planId);
    List<PlannedActivity> findByActivityId(UUID activityId);
}