package com.moroccanvviptrip.api.mvtapi.servicesTests;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.domain.PlannedActivity;
import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.repository.PlanRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.PlannedActivityService;
import com.moroccanvviptrip.api.mvtapi.services.UserService;
import com.moroccanvviptrip.api.mvtapi.services.impl.PlanServiceImpl;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.PlanMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlanServiceTests {
    private PlanRepository planRepository;
    private UserService userService;
    private PlanMapper planMapper;
    private FileStorageService fileStorageService;
    private PlanServiceImpl planService;
    private final UUID planId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final UUID activityId = UUID.randomUUID();

    @BeforeEach
    public void beforeEach() {
        planRepository = mock(PlanRepository.class);
        userService = mock(UserService.class);
        ActivityService activityService = mock(ActivityService.class);
        planMapper = mock(PlanMapper.class);
        fileStorageService = mock(FileStorageService.class);
        PlannedActivityService plannedActivityService = mock(PlannedActivityService.class);
        planService = new PlanServiceImpl(
                planRepository,
                userService,
                activityService,
                planMapper,
                fileStorageService,
                plannedActivityService
        );
    }

    @Test
    public void findById_shouldReturnPlan_whenPlanExists() {
        // Arrange
        Plan expectedPlan = createPlan();
        when(planRepository.findById(planId)).thenReturn(Optional.of(expectedPlan));

        // Act
        Plan result = planService.findById(planId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPlan.getId(), result.getId());
        assertEquals(expectedPlan.getName(), result.getName());
        assertEquals(expectedPlan.getDescription(), result.getDescription());
    }

    @Test
    public void findById_shouldThrowEntityNotFoundException_whenPlanDoesNotExist() {
        // Arrange
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> planService.findById(planId)
        );
        assertEquals("Plan not found with ID: " + planId, exception.getMessage());
    }

    @Test
    public void findAll_shouldReturnAllPlans() {
        // Arrange
        List<Plan> expectedPlans = createPlanList(3);
        when(planRepository.findAll()).thenReturn(expectedPlans);

        // Act
        List<Plan> result = planService.findAll();

        // Assert
        assertEquals(expectedPlans.size(), result.size());
        assertEquals(expectedPlans, result);
    }

    @Test
    public void findAll_withPageable_shouldReturnPageOfPlans() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Plan> plans = createPlanList(3);
        Page<Plan> expectedPage = new PageImpl<>(plans, pageable, plans.size());

        when(planRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Plan> result = planService.findAll(pageable);

        // Assert
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent(), result.getContent());
    }

    @Test
    public void findUserPlans_shouldReturnUserPlans() {
        // Arrange
        List<Plan> expectedPlans = createPlanList(2);
        User user = User.builder()
                .id(userId)
                .plans(expectedPlans)
                .build();

        when(userService.findById(userId)).thenReturn(user);

        // Act
        List<Plan> result = planService.findUserPlans(userId);

        // Assert
        assertEquals(expectedPlans.size(), result.size());
        assertEquals(expectedPlans, result);
    }

    @Test
    public void findUserPlans_withPageable_shouldReturnPageOfUserPlans() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Plan> plans = createPlanList(2);
        Page<Plan> expectedPage = new PageImpl<>(plans, pageable, plans.size());

        User currentUser = User.builder()
                .id(userId)
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(planRepository.findByUserId(userId, pageable)).thenReturn(expectedPage);

        // Act
        Page<Plan> result = planService.findUserPlans(pageable);

        // Assert
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent(), result.getContent());
    }

    @Test
    public void create_shouldCreatePlan_whenValidRequest() {
        // Arrange
        User currentUser = User.builder()
                .id(userId)
                .build();

        MultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        PlanRequestDto requestDto = PlanRequestDto.builder()
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .imageUri(imageFile)
                .build();

        Plan unsavedPlan = Plan.builder()
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .build();

        Plan savedPlan = Plan.builder()
                .id(planId)
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .imageUrl("http://localhost:8080/api/v1/files/test-image.jpg")
                .user(currentUser)
                .plannedActivities(new ArrayList<>())
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(planMapper.toEntity(requestDto)).thenReturn(unsavedPlan);
        when(fileStorageService.store(imageFile)).thenReturn("test-image.jpg");
        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        // Act
        Plan result = planService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(planId, result.getId());
        assertEquals("Morocco Adventure", result.getName());
        assertEquals("A wonderful adventure in Morocco", result.getDescription());
        assertEquals("http://localhost:8080/api/v1/files/test-image.jpg", result.getImageUrl());
        assertEquals(currentUser, result.getUser());
        assertNotNull(result.getPlannedActivities());
        assertTrue(result.getPlannedActivities().isEmpty());
    }

    @Test
    public void create_shouldCreatePlanWithoutImage_whenImageIsNull() {
        // Arrange
        User currentUser = User.builder()
                .id(userId)
                .build();

        PlanRequestDto requestDto = PlanRequestDto.builder()
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .imageUri(null)
                .build();

        Plan unsavedPlan = Plan.builder()
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .build();

        Plan savedPlan = Plan.builder()
                .id(planId)
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .user(currentUser)
                .plannedActivities(new ArrayList<>())
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(planMapper.toEntity(requestDto)).thenReturn(unsavedPlan);
        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        // Act
        Plan result = planService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(planId, result.getId());
        assertEquals("Morocco Adventure", result.getName());
        assertEquals("A wonderful adventure in Morocco", result.getDescription());
        assertNull(result.getImageUrl());
        assertEquals(currentUser, result.getUser());
        assertNotNull(result.getPlannedActivities());
        assertTrue(result.getPlannedActivities().isEmpty());
    }

    @Test
    public void update_shouldUpdatePlan_whenPlanExists() {
        // Arrange
        User currentUser = User.builder()
                .id(userId)
                .build();

        Plan existingPlan = Plan.builder()
                .id(planId)
                .name("Old Plan Name")
                .description("Old description")
                .imageUrl("http://localhost:8080/api/v1/files/old-image.jpg")
                .user(currentUser)
                .build();

        MultipartFile newImageFile = new MockMultipartFile(
                "newImageFile",
                "new-image.jpg",
                "image/jpeg",
                "new image content".getBytes()
        );

        PlanUpdateDto updateDto = PlanUpdateDto.builder()
                .name("Updated Plan Name")
                .description("Updated description")
                .imageUri(newImageFile)
                .build();

        Plan updatedPlan = Plan.builder()
                .id(planId)
                .name("Updated Plan Name")
                .description("Updated description")
                .imageUrl("http://localhost:8080/api/v1/files/new-image.jpg")
                .user(currentUser)
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(fileStorageService.store(newImageFile)).thenReturn("new-image.jpg");
        when(planRepository.save(any(Plan.class))).thenReturn(updatedPlan);

        // Act
        Plan result = planService.update(planId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(planId, result.getId());
        assertEquals("Updated Plan Name", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals("http://localhost:8080/api/v1/files/new-image.jpg", result.getImageUrl());
    }

    @Test
    public void update_shouldThrowSecurityException_whenUserDoesNotOwnPlan() {
        // Arrange
        User planOwner = User.builder()
                .id(UUID.randomUUID())
                .build();

        User currentUser = User.builder()
                .id(userId)
                .build();

        Plan existingPlan = Plan.builder()
                .id(planId)
                .name("Old Plan Name")
                .description("Old description")
                .user(planOwner)
                .build();

        PlanUpdateDto updateDto = PlanUpdateDto.builder()
                .name("Updated Plan Name")
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> planService.update(planId, updateDto)
        );
        assertEquals("You don't have permission to update this plan", exception.getMessage());
    }

    @Test
    public void delete_shouldDeletePlan_whenPlanExists() {
        // Arrange
        User currentUser = User.builder()
                .id(userId)
                .build();

        Plan existingPlan = Plan.builder()
                .id(planId)
                .name("Plan to Delete")
                .imageUrl("http://localhost:8080/api/v1/files/image.jpg")
                .user(currentUser)
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        // Act
        planService.delete(planId);

        // Assert - We can't directly assert deletion, but we can capture the argument to verify
        ArgumentCaptor<Plan> planCaptor = ArgumentCaptor.forClass(Plan.class);
        verify(planRepository).delete(planCaptor.capture());
        assertEquals(planId, planCaptor.getValue().getId());
        verify(fileStorageService).delete("image.jpg");
    }

    @Test
    public void delete_shouldThrowSecurityException_whenUserDoesNotOwnPlan() {
        // Arrange
        User planOwner = User.builder()
                .id(UUID.randomUUID())
                .build();

        User currentUser = User.builder()
                .id(userId)
                .build();

        Plan existingPlan = Plan.builder()
                .id(planId)
                .name("Plan to Delete")
                .user(planOwner)
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> planService.delete(planId)
        );
        assertEquals("You don't have permission to delete this plan", exception.getMessage());
    }

    @Test
    public void addActivityToPlan_shouldThrowSecurityException_whenUserDoesNotOwnPlan() {
        // Arrange
        User planOwner = User.builder()
                .id(UUID.randomUUID())
                .build();

        User currentUser = User.builder()
                .id(userId)
                .build();

        Plan existingPlan = Plan.builder()
                .id(planId)
                .name("Adventure Plan")
                .user(planOwner)
                .build();

        PlannedActivityRequestDto requestDto = new PlannedActivityRequestDto();

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> planService.addActivityToPlan(planId, activityId, requestDto)
        );
        assertEquals("You don't have permission to modify this plan", exception.getMessage());
    }


    // Helper methods to create test data
    private Plan createPlan() {
        User user = User.builder()
                .id(userId)
                .build();

        return Plan.builder()
                .id(planId)
                .name("Morocco Adventure")
                .description("A wonderful adventure in Morocco")
                .imageUrl("http://localhost:8080/api/v1/files/test-image.jpg")
                .user(user)
                .plannedActivities(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<Plan> createPlanList(int count) {
        List<Plan> plans = new ArrayList<>();
        User user = User.builder()
                .id(userId)
                .build();

        for (int i = 0; i < count; i++) {
            plans.add(Plan.builder()
                    .id(UUID.randomUUID())
                    .name("Plan " + (i + 1))
                    .description("Description " + (i + 1))
                    .imageUrl("http://localhost:8080/api/v1/files/image" + (i + 1) + ".jpg")
                    .user(user)
                    .plannedActivities(new ArrayList<>())
                    .createdAt(LocalDateTime.now())
                    .build());
        }
        return plans;
    }
}