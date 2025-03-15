package com.moroccanvviptrip.api.mvtapi.servicesTests;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.repository.CategoryRepository;
import com.moroccanvviptrip.api.mvtapi.services.impl.CategoryServiceImpl;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.exception.PropertyExistsException;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private FileStorageService fileStorageService;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void beforeEach() {
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        fileStorageService = mock(FileStorageService.class);
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper, fileStorageService);
    }

    @Test
    public void findAll_shouldReturnAllCategories() {
        // Arrange
        List<Category> expectedCategories = List.of(
                Category.builder()
                        .id(1L)
                        .name("Adventure")
                        .description("Adventure activities")
                        .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                        .build(),
                Category.builder()
                        .id(2L)
                        .name("Relaxation")
                        .description("Relaxation activities")
                        .imageUri("http://localhost:8080/api/v1/files/relaxation.jpg")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // Act
        List<Category> result = categoryService.findAll();

        // Assert
        assertEquals(expectedCategories.size(), result.size(), "The result should have the same size as the expected categories");
        assertEquals(expectedCategories, result, "The result should match the expected categories");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void findById_shouldReturnCategory_whenCategoryExists() {
        // Arrange
        Category expectedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expectedCategory));

        // Act
        Category result = categoryService.findById(1L);

        // Assert
        assertEquals(expectedCategory, result, "The result should match the expected category");
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(1L),
                "Should throw EntityNotFoundException when category does not exist"
        );
        assertEquals("Category not found!", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void create_shouldCreateCategory_whenValidRequest() {
        // Arrange
        MultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Adventure");
        requestDto.setDescription("Adventure activities");
        requestDto.setImageUri(imageFile);

        Category categoryEntity = Category.builder()
                .name("Adventure")
                .description("Adventure activities")
                .build();

        Category savedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/test-image.jpg")
                .build();

        when(categoryRepository.existsByName("Adventure")).thenReturn(false);
        when(categoryMapper.toEntity(requestDto)).thenReturn(categoryEntity);
        when(fileStorageService.store(imageFile)).thenReturn("test-image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        Category result = categoryService.create(requestDto);

        // Assert
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals("http://localhost:8080/api/v1/files/test-image.jpg", capturedCategory.getImageUri());
        assertEquals(savedCategory, result);
        verify(categoryRepository, times(1)).existsByName("Adventure");
        verify(fileStorageService, times(1)).store(imageFile);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void create_shouldThrowPropertyExistsException_whenCategoryNameExists() {
        // Arrange
        MultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Adventure");
        requestDto.setDescription("Adventure activities");
        requestDto.setImageUri(imageFile);

        when(categoryRepository.existsByName("Adventure")).thenReturn(true);

        // Act & Assert
        PropertyExistsException exception = assertThrows(PropertyExistsException.class,
                () -> categoryService.create(requestDto),
                "Should throw PropertyExistsException when category name already exists"
        );
        assertEquals("Category with this name already exists.", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName("Adventure");
        verify(fileStorageService, never()).store(any(MultipartFile.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void create_shouldThrowIllegalArgumentException_whenImageIsNull() {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Adventure");
        requestDto.setDescription("Adventure activities");
        requestDto.setImageUri(null);

        when(categoryRepository.existsByName("Adventure")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.create(requestDto),
                "Should throw IllegalArgumentException when image is null"
        );
        assertEquals("Image file is required.", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName("Adventure");
        verify(fileStorageService, never()).store(any(MultipartFile.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void delete_shouldDeleteCategory_whenCategoryExists() {
        // Arrange
        Category category = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        categoryService.delete(1L);

        // Assert
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).delete("adventure.jpg");
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void delete_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.delete(1L),
                "Should throw EntityNotFoundException when category does not exist"
        );
        assertEquals("Category not found!", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, never()).delete(anyString());
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    public void update_shouldUpdateDescriptionOnly_whenOnlyDescriptionProvided() {
        // Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Old description")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        CategoryUpdateDto updateDto = new CategoryUpdateDto();
        updateDto.setDescription("Updated description");

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Updated description")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        Category result = categoryService.update(1L, updateDto);

        // Assert
        assertEquals(updatedCategory, result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, never()).delete(anyString());
        verify(fileStorageService, never()).store(any(MultipartFile.class));

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals("Updated description", capturedCategory.getDescription());
        assertEquals("http://localhost:8080/api/v1/files/adventure.jpg", capturedCategory.getImageUri());
    }

    @Test
    public void update_shouldUpdateImageOnly_whenOnlyImageProvided() {
        // Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        MultipartFile newImageFile = new MockMultipartFile(
                "imageFile",
                "new-image.jpg",
                "image/jpeg",
                "new image content".getBytes()
        );

        CategoryUpdateDto updateDto = new CategoryUpdateDto();
        updateDto.setImageUri(newImageFile);

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/new-image.jpg")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(fileStorageService.store(newImageFile)).thenReturn("new-image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        Category result = categoryService.update(1L, updateDto);

        // Assert
        assertEquals(updatedCategory, result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).delete("adventure.jpg");
        verify(fileStorageService, times(1)).store(newImageFile);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals("Adventure activities", capturedCategory.getDescription());
        assertEquals("http://localhost:8080/api/v1/files/new-image.jpg", capturedCategory.getImageUri());
    }

    @Test
    public void update_shouldUpdateBothDescriptionAndImage_whenBothProvided() {
        // Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Old description")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        MultipartFile newImageFile = new MockMultipartFile(
                "imageFile",
                "new-image.jpg",
                "image/jpeg",
                "new image content".getBytes()
        );

        CategoryUpdateDto updateDto = new CategoryUpdateDto();
        updateDto.setDescription("Updated description");
        updateDto.setImageUri(newImageFile);

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Updated description")
                .imageUri("http://localhost:8080/api/v1/files/new-image.jpg")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(fileStorageService.store(newImageFile)).thenReturn("new-image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        Category result = categoryService.update(1L, updateDto);

        // Assert
        assertEquals(updatedCategory, result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).delete("adventure.jpg");
        verify(fileStorageService, times(1)).store(newImageFile);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals("Updated description", capturedCategory.getDescription());
        assertEquals("http://localhost:8080/api/v1/files/new-image.jpg", capturedCategory.getImageUri());
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenCategoryDoesNotExist() {
        // Arrange
        CategoryUpdateDto updateDto = new CategoryUpdateDto();
        updateDto.setDescription("Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(1L, updateDto),
                "Should throw EntityNotFoundException when category does not exist"
        );
        assertEquals("Category not found!", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
        verify(fileStorageService, never()).delete(anyString());
        verify(fileStorageService, never()).store(any(MultipartFile.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }
}