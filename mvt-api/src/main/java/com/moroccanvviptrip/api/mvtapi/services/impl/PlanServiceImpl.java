package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.PlanRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.PlanService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.PlanMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final PlanMapper planMapper;

    @Override
    public Plan findById(UUID id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with ID: " + id));
    }

    @Override
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Override
    public Page<Plan> findAll(Pageable pageable) {
        return planRepository.findAll(pageable);
    }

    @Override
    public List<Plan> findUserPlans(UUID userId) {
        User user = userService.findById(userId);
        return user.getPlans();
    }

    @Override
    public Page<Plan> findUserPlans(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        UUID userId = currentUser.getId();
        return planRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Plan create(PlanRequestDto planRequestDto) {
        User currentUser = userService.getCurrentUser();

        Plan plan = planMapper.toEntity(planRequestDto);
        plan.setUser(currentUser);
        plan.setPlannedActivities(new ArrayList<>());

        return planRepository.save(plan);
    }

    @Override
    @Transactional
    public Plan update(UUID id, PlanUpdateDto planUpdateDto) {
        Plan existingPlan = findById(id);

        User currentUser = userService.getCurrentUser();
        if (!existingPlan.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to update this plan");
        }

        if (planUpdateDto.getName() != null && !planUpdateDto.getName().isEmpty()) {
            existingPlan.setName(planUpdateDto.getName());
        }

        if (planUpdateDto.getDescription() != null) {
            existingPlan.setDescription(planUpdateDto.getDescription());
        }

        return planRepository.save(existingPlan);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Plan plan = findById(id);

        User currentUser = userService.getCurrentUser();
        if (!plan.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to delete this plan");
        }

        planRepository.delete(plan);
    }

    @Override
    @Transactional
    public void addActivityToPlan(UUID planId, UUID activityId, PlannedActivityRequestDto plannedActivityRequestDto) {
        Plan plan = findById(planId);
        Activity activity = activityService.findById(activityId);

        User currentUser = userService.getCurrentUser();
        if (!plan.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to modify this plan");
        }

        PlannedActivity plannedActivity = PlannedActivity.builder()
                .plan(plan)
                .activity(activity)
                .priority(plannedActivityRequestDto.getPriority())
                .startDate(plannedActivityRequestDto.getStartDate())
                .endDate(plannedActivityRequestDto.getEndDate())
                .build();

        if (plan.getPlannedActivities() == null) {
            plan.setPlannedActivities(new ArrayList<>());
        }
        plan.getPlannedActivities().add(plannedActivity);

        planRepository.save(plan);
    }

    @Override
    @Transactional
    public void removeActivityFromPlan(UUID planId, UUID activityId) {
        Plan plan = findById(planId);

        User currentUser = userService.getCurrentUser();
        if (!plan.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to modify this plan");
        }

        plan.getPlannedActivities().removeIf(plannedActivity ->
                plannedActivity.getActivity().getId().equals(activityId));

        planRepository.save(plan);
    }
}