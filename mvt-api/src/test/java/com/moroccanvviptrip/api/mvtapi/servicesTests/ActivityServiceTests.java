package com.moroccanvviptrip.api.mvtapi.servicesTests;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.repository.ActivityRepository;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.services.impl.ActivityServiceImpl;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.utils.specifications.ActivitySpecifications;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ActivityMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActivityServiceTests {
    private ActivityRepository activityRepository;
    private CategoryService categoryService;
    private CityService cityService;
    private ActivityMapper activityMapper;
    private ActivitySpecifications activitySpecifications;
    private FileStorageService fileStorageService;
    private ActivityServiceImpl activityService;

    private final UUID activityId = UUID.randomUUID();
    private final Long categoryId = 1L;
    private final Long cityId = 1L;

    @BeforeEach
    public void beforeEach() {
        activityRepository = mock(ActivityRepository.class);
        categoryService = mock(CategoryService.class);
        cityService = mock(CityService.class);
        activityMapper = mock(ActivityMapper.class);
        activitySpecifications = mock(ActivitySpecifications.class);
        fileStorageService = mock(FileStorageService.class);

        activityService = new ActivityServiceImpl(
                activityRepository,
                categoryService,
                cityService,
                activityMapper,
                activitySpecifications,
                fileStorageService
        );
    }

    @Test
    public void findById_shouldReturnActivity_whenActivityExists() {
        // Arrange
        Activity expectedActivity = createActivity();
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(expectedActivity));

        // Act
        Activity result = activityService.findById(activityId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedActivity.getId(), result.getId());
        assertEquals(expectedActivity.getName(), result.getName());
    }

    @Test
    public void findById_shouldThrowEntityNotFoundException_whenActivityDoesNotExist() {
        // Arrange
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> activityService.findById(activityId)
        );
        assertEquals("Activity not found", exception.getMessage());
    }

    @Test
    public void findTopActivities_shouldReturnLimitedActivities() {
        // Arrange
        int limit = 5;
        List<Activity> expectedActivities = createActivityList(limit);

        when(activitySpecifications.isAvailable(true)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("available"), true));
        when(activityRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedActivities));

        // Act
        List<Activity> result = activityService.findTopActivities(limit);

        // Assert
        assertEquals(limit, result.size());
        assertEquals(expectedActivities, result);
    }

    @Test
    public void findAll_shouldReturnPageOfActivities() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Activity> expectedActivities = createActivityList(10);
        Page<Activity> expectedPage = new PageImpl<>(expectedActivities, pageable, expectedActivities.size());

        when(activitySpecifications.hasCity(cityId)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("city").get("id"), cityId));
        when(activitySpecifications.hasCategory(categoryId)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        when(activitySpecifications.isAvailable(true)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("available"), true));
        when(activitySpecifications.containsKeyword("test")).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%test%"));

        when(activityRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Activity> result = activityService.findAll(cityId, categoryId, true, "test", pageable);

        // Assert
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent(), result.getContent());
    }

    @Test
    public void create_shouldCreateActivity_whenValidRequest() {
        // Arrange
        Category category = Category.builder().id(categoryId).name("Adventure").build();
        City city = new City(cityId, "Marrakech", "Marrakech-Safi");

        MultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        ActivityRequestDto requestDto = ActivityRequestDto.builder()
                .name("Desert Tour")
                .description("Amazing desert tour experience")
                .location("Merzouga")
                .categoryId(categoryId)
                .cityId(cityId)
                .imageUri(imageFile)
                .build();

        Activity unsavedActivity = Activity.builder()
                .name("Desert Tour")
                .description("Amazing desert tour experience")
                .location("Merzouga")
                .build();

        Activity savedActivity = Activity.builder()
                .id(activityId)
                .name("Desert Tour")
                .description("Amazing desert tour experience")
                .location("Merzouga")
                .imageUri("http://localhost:8080/api/v1/files/test-image.jpg")
                .category(category)
                .city(city)
                .available(true)
                .build();

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(cityService.findById(cityId)).thenReturn(city);
        when(activityMapper.toEntity(requestDto)).thenReturn(unsavedActivity);
        when(fileStorageService.store(imageFile)).thenReturn("test-image.jpg");
        when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

        // Act
        Activity result = activityService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(activityId, result.getId());
        assertEquals("Desert Tour", result.getName());
        assertEquals("http://localhost:8080/api/v1/files/test-image.jpg", result.getImageUri());
        assertTrue(result.isAvailable());
        assertEquals(category, result.getCategory());
        assertEquals(city, result.getCity());
    }

    @Test
    public void create_shouldThrowIllegalArgumentException_whenImageIsNull() {
        // Arrange
        ActivityRequestDto requestDto = ActivityRequestDto.builder()
                .name("Desert Tour")
                .description("Amazing desert tour experience")
                .location("Merzouga")
                .categoryId(categoryId)
                .cityId(cityId)
                .imageUri(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> activityService.create(requestDto)
        );
        assertEquals("Image file is required.", exception.getMessage());
    }

    @Test
    public void update_shouldUpdateActivity_whenActivityExists() {
        // Arrange
        Category newCategory = Category.builder().id(2L).name("Relaxation").build();
        City newCity = new City(2L, "Fes", "Fes-Meknes");

        MultipartFile newImageFile = new MockMultipartFile(
                "newImageFile",
                "new-image.jpg",
                "image/jpeg",
                "new image content".getBytes()
        );

        Activity existingActivity = Activity.builder()
                .id(activityId)
                .name("Desert Tour")
                .description("Old description")
                .location("Merzouga")
                .imageUri("http://localhost:8080/api/v1/files/old-image.jpg")
                .category(Category.builder().id(categoryId).build())
                .city(new City(cityId, "Marrakech", "Marrakech-Safi"))
                .available(true)
                .build();

        Activity updatedActivity = Activity.builder()
                .id(activityId)
                .name("Updated Tour")
                .description("Updated description")
                .location("Updated location")
                .imageUri("http://localhost:8080/api/v1/files/new-image.jpg")
                .category(newCategory)
                .city(newCity)
                .available(false)
                .build();

        ActivityUpdateDto updateDto = ActivityUpdateDto.builder()
                .name("Updated Tour")
                .description("Updated description")
                .location("Updated location")
                .categoryId(2L)
                .cityId(2L)
                .available(false)
                .imageUri(newImageFile)
                .build();

        when(activityRepository.findById(activityId)).thenReturn(Optional.of(existingActivity));
        when(categoryService.findById(2L)).thenReturn(newCategory);
        when(cityService.findById(2L)).thenReturn(newCity);
        when(fileStorageService.store(newImageFile)).thenReturn("new-image.jpg");
        when(activityRepository.save(any(Activity.class))).thenReturn(updatedActivity);

        doAnswer(invocation -> {
            Activity activity = invocation.getArgument(1);
            activity.setName(updateDto.getName());
            activity.setDescription(updateDto.getDescription());
            activity.setLocation(updateDto.getLocation());
            activity.setAvailable(updateDto.getAvailable());
            return null;
        }).when(activityMapper).partialUpdate(eq(updateDto), any(Activity.class));

        // Act
        Activity result = activityService.update(activityId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(activityId, result.getId());
        assertEquals("Updated Tour", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals("Updated location", result.getLocation());
        assertEquals("http://localhost:8080/api/v1/files/new-image.jpg", result.getImageUri());
        assertEquals(newCategory, result.getCategory());
        assertEquals(newCity, result.getCity());
        assertFalse(result.isAvailable());
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenActivityDoesNotExist() {
        // Arrange
        ActivityUpdateDto updateDto = ActivityUpdateDto.builder()
                .name("Updated Tour")
                .build();

        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> activityService.update(activityId, updateDto)
        );
        assertEquals("Activity not found with ID: " + activityId, exception.getMessage());
    }

    @Test
    public void delete_shouldDeleteActivity_whenActivityExists() {
        // Arrange
        Activity existingActivity = Activity.builder()
                .id(activityId)
                .name("Desert Tour")
                .imageUri("http://localhost:8080/api/v1/files/image.jpg")
                .build();

        when(activityRepository.findById(activityId)).thenReturn(Optional.of(existingActivity));

        // Act
        activityService.delete(activityId);

        // Assert - We can't directly assert deletion, but we can capture the argument to verify
        ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).delete(activityCaptor.capture());
        assertEquals(activityId, activityCaptor.getValue().getId());
        verify(fileStorageService).delete("image.jpg");
    }

    @Test
    public void delete_shouldThrowEntityNotFoundException_whenActivityDoesNotExist() {
        // Arrange
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> activityService.delete(activityId)
        );
        assertEquals("Activity not found with ID: " + activityId, exception.getMessage());
    }

    // Helper methods to create test data
    private Activity createActivity() {
        return Activity.builder()
                .id(activityId)
                .name("Desert Tour")
                .description("Amazing desert tour experience")
                .location("Merzouga")
                .imageUri("http://localhost:8080/api/v1/files/test-image.jpg")
                .category(Category.builder().id(categoryId).build())
                .city(new City(cityId, "Marrakech", "Marrakech-Safi"))
                .available(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<Activity> createActivityList(int count) {
        List<Activity> activities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            activities.add(Activity.builder()
                    .id(UUID.randomUUID())
                    .name("Activity " + (i + 1))
                    .description("Description " + (i + 1))
                    .location("Location " + (i + 1))
                    .imageUri("http://localhost:8080/api/v1/files/image" + (i + 1) + ".jpg")
                    .category(Category.builder().id(categoryId).build())
                    .city(new City(cityId, "Marrakech", "Marrakech-Safi"))
                    .available(true)
                    .createdAt(LocalDateTime.now())
                    .build());
        }
        return activities;
    }
}