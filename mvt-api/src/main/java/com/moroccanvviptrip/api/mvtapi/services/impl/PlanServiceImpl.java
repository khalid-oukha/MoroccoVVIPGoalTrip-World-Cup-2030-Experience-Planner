package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.PlanRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.PlanService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.PlanMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final PlanMapper planMapper;
    private final FileStorageService fileStorageService;

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
        log.info("Creating plan with name: {}", planRequestDto.getName());
        User currentUser = userService.getCurrentUser();

        Plan plan = planMapper.toEntity(planRequestDto);
        plan.setName(planRequestDto.getName());
        plan.setDescription(planRequestDto.getDescription());
        plan.setUser(currentUser);
        plan.setPlannedActivities(new ArrayList<>());

        // Handle image upload
        MultipartFile imageFile = planRequestDto.getImageUri();
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Processing image file of size: {}", imageFile.getSize());
            try {
                String fileName = fileStorageService.store(imageFile);
                String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;
                log.info("Image stored successfully with URL: {}", imageUrl);
                plan.setImageUrl(imageUrl);
            } catch (Exception e) {
                log.error("Error storing image file", e);
                throw new RuntimeException("Failed to store image file", e);
            }
        } else {
            log.info("No image file provided for plan");
        }

        Plan savedPlan = planRepository.save(plan);
        log.info("Plan created successfully with ID: {}", savedPlan.getId());
        return savedPlan;
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

        // Handle image update
        MultipartFile newImageFile = planUpdateDto.getImageUri();
        if (newImageFile != null && !newImageFile.isEmpty()) {
            log.info("Updating image for plan ID: {}", id);

            // Delete the old image if it exists
            if (existingPlan.getImageUrl() != null && !existingPlan.getImageUrl().isEmpty()) {
                String oldFileName = existingPlan.getImageUrl()
                        .substring(existingPlan.getImageUrl().lastIndexOf("/") + 1);
                fileStorageService.delete(oldFileName);
            }

            // Store the new image
            try {
                String newFileName = fileStorageService.store(newImageFile);
                String newImageUrl = "http://localhost:8080/api/v1/files/" + newFileName;
                log.info("New image stored with URL: {}", newImageUrl);
                existingPlan.setImageUrl(newImageUrl);
            } catch (Exception e) {
                log.error("Error updating image file", e);
                throw new RuntimeException("Failed to update image file", e);
            }
        }

        Plan updatedPlan = planRepository.save(existingPlan);
        log.info("Plan updated successfully with ID: {}", updatedPlan.getId());
        return updatedPlan;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Plan plan = findById(id);

        User currentUser = userService.getCurrentUser();
        if (!plan.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to delete this plan");
        }

        // Delete the image if it exists
        if (plan.getImageUrl() != null && !plan.getImageUrl().isEmpty()) {
            String fileName = plan.getImageUrl()
                    .substring(plan.getImageUrl().lastIndexOf("/") + 1);
            fileStorageService.delete(fileName);
        }

        planRepository.delete(plan);
        log.info("Plan deleted successfully with ID: {}", id);
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
        log.info("Activity added to plan. Plan ID: {}, Activity ID: {}", planId, activityId);
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
        log.info("Activity removed from plan. Plan ID: {}, Activity ID: {}", planId, activityId);
    }
}