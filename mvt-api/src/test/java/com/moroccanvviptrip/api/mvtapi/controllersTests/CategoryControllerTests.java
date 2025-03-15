package com.moroccanvviptrip.api.mvtapi.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CategoryResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CategoryMapper;
import com.moroccanvviptrip.api.mvtapi.web.rest.CategoryController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .build();
    }

    @Test
    public void getAll_shouldReturnAllCategories() throws Exception {
        Category category1 = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        Category category2 = Category.builder()
                .id(2L)
                .name("Relaxation")
                .description("Relaxation activities")
                .imageUri("http://localhost:8080/api/v1/files/relaxation.jpg")
                .build();

        List<Category> categories = Arrays.asList(category1, category2);

        CategoryResponseVm responseVm1 = CategoryResponseVm.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        CategoryResponseVm responseVm2 = CategoryResponseVm.builder()
                .id(2L)
                .name("Relaxation")
                .description("Relaxation activities")
                .imageUri("http://localhost:8080/api/v1/files/relaxation.jpg")
                .build();

        when(categoryService.findAll()).thenReturn(categories);
        when(categoryMapper.toResponse(category1)).thenReturn(responseVm1);
        when(categoryMapper.toResponse(category2)).thenReturn(responseVm2);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Adventure")))
                .andExpect(jsonPath("$[0].description", is("Adventure activities")))
                .andExpect(jsonPath("$[0].imageUri", is("http://localhost:8080/api/v1/files/adventure.jpg")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Relaxation")))
                .andExpect(jsonPath("$[1].description", is("Relaxation activities")))
                .andExpect(jsonPath("$[1].imageUri", is("http://localhost:8080/api/v1/files/relaxation.jpg")));
    }

    @Test
    public void getById_shouldReturnCategory_whenCategoryExists() throws Exception {
        Category category = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        CategoryResponseVm responseVm = CategoryResponseVm.builder()
                .id(1L)
                .name("Adventure")
                .description("Adventure activities")
                .imageUri("http://localhost:8080/api/v1/files/adventure.jpg")
                .build();

        when(categoryService.findById(1L)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(responseVm);

        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Adventure")))
                .andExpect(jsonPath("$.description", is("Adventure activities")))
                .andExpect(jsonPath("$.imageUri", is("http://localhost:8080/api/v1/files/adventure.jpg")));
    }

    @Test
    public void delete_shouldReturnNoContent_whenCategoryExists() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCategory_shouldUpdateAndReturnCategory() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageUri",
                "updated-image.jpg",
                "image/jpeg",
                "updated image content".getBytes()
        );

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Adventure")
                .description("Updated description")
                .imageUri("http://localhost:8080/api/v1/files/updated-image.jpg")
                .build();

        CategoryResponseVm responseVm = CategoryResponseVm.builder()
                .id(1L)
                .name("Adventure")
                .description("Updated description")
                .imageUri("http://localhost:8080/api/v1/files/updated-image.jpg")
                .build();

        when(categoryService.update(eq(1L), any(CategoryUpdateDto.class))).thenReturn(updatedCategory);
        when(categoryMapper.toResponse(updatedCategory)).thenReturn(responseVm);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/categories/1")
                        .file(imageFile)
                        .param("description", "Updated description")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Adventure")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.imageUri", is("http://localhost:8080/api/v1/files/updated-image.jpg")));
    }

}