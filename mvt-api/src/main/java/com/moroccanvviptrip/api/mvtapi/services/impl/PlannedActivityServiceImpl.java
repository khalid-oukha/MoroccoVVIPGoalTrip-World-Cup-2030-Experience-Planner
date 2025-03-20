package com.moroccanvviptrip.api.mvtapi.services.impl;


import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.PlannedActivityRepository;

import com.moroccanvviptrip.api.mvtapi.services.PlannedActivityService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlannedActivityServiceImpl implements PlannedActivityService {

    private final PlannedActivityRepository plannedActivityRepository;
    private final UserService userService;


    @Override
    public PlannedActivity findById(UUID id) {
        return plannedActivityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Planned activity not found with ID: " + id));
    }

    @Override
    public List<PlannedActivity> findByPlanId(UUID planId) {
        return plannedActivityRepository.findByPlanId(planId);
    }

    @Override
    @Transactional
    public PlannedActivity update(UUID id, PlannedActivityUpdateDto updateDto) {
        PlannedActivity existingActivity = findById(id);

        User currentUser = userService.getCurrentUser();
        if (!existingActivity.getPlan().getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to update this planned activity");
        }

        LocalDateTime startDate = updateDto.getStartDate() != null ? updateDto.getStartDate() : existingActivity.getStartDate();
        LocalDateTime endDate = updateDto.getEndDate() != null ? updateDto.getEndDate() : existingActivity.getEndDate();

        validateDates(startDate, endDate);

        if (updateDto.getPriority() != null) {
            existingActivity.setPriority(updateDto.getPriority());
        }

        if (updateDto.getStartDate() != null) {
            existingActivity.setStartDate(startDate);
        }

        if (updateDto.getEndDate() != null) {
            existingActivity.setEndDate(endDate);
        }

        PlannedActivity updatedActivity = plannedActivityRepository.save(existingActivity);
        log.info("Planned activity updated successfully with ID: {}", updatedActivity.getId());
        return updatedActivity;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        PlannedActivity plannedActivity = findById(id);

        User currentUser = userService.getCurrentUser();
        if (!plannedActivity.getPlan().getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to delete this planned activity");
        }

        plannedActivityRepository.delete(plannedActivity);
        log.info("Planned activity deleted successfully with ID: {}", id);
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }

        LocalDateTime now = LocalDateTime.now();

        if (startDate.isBefore(now.minusMinutes(1))) {
            throw new IllegalArgumentException("Start date must be in the present or future");
        }

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}