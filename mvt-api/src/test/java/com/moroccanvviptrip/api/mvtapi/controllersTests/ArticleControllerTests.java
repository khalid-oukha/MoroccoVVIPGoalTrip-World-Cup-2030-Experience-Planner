package com.moroccanvviptrip.api.mvtapi.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.services.ArticleService;
import com.moroccanvviptrip.api.mvtapi.web.VM.ArticleResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ArticleMapper;
import com.moroccanvviptrip.api.mvtapi.web.rest.ArticleController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArticleControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleController articleController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final UUID articleId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
    private final UUID activityId = UUID.fromString("d47ac10b-58cc-4372-a567-0e02b2c3d480");
    private final LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .build();
    }

    @Test
    public void findById_shouldReturnArticle_whenArticleExists() throws Exception {
        Activity activity = Activity.builder()
                .id(activityId)
                .name("Desert Safari")
                .build();

        Article article = new Article();
        article.setId(articleId);
        article.setContent("Detailed article content");
        article.setImageUrl("http://localhost:8080/api/v1/files/image.jpg");
        article.setActivity(activity);
        article.setCreatedAt(createdAt);

        ArticleResponseVm responseVm = ArticleResponseVm.builder()
                .id(articleId)
                .content("Detailed article content")
                .imageUrl("http://localhost:8080/api/v1/files/image.jpg")
                .createdAt(createdAt)
                .build();

        when(articleService.findById(articleId)).thenReturn(article);
        when(articleMapper.toResponseVm(article)).thenReturn(responseVm);

        mockMvc.perform(get("/api/v1/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(articleId.toString())))
                .andExpect(jsonPath("$.content", is("Detailed article content")))
                .andExpect(jsonPath("$.imageUrl", is("http://localhost:8080/api/v1/files/image.jpg")));
    }

    @Test
    public void update_shouldUpdateAndReturnArticle() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "updated-image.jpg",
                "image/jpeg",
                "updated image content".getBytes()
        );

        Activity activity = Activity.builder()
                .id(activityId)
                .name("Desert Safari")
                .build();

        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setContent("Updated article content");
        updatedArticle.setImageUrl("http://localhost:8080/api/v1/files/updated-image.jpg");
        updatedArticle.setActivity(activity);
        updatedArticle.setCreatedAt(createdAt);

        ArticleResponseVm responseVm = ArticleResponseVm.builder()
                .id(articleId)
                .content("Updated article content")
                .imageUrl("http://localhost:8080/api/v1/files/updated-image.jpg")
                .createdAt(createdAt)
                .build();

        when(articleService.update(eq(articleId), any(ArticleUpdateDto.class))).thenReturn(updatedArticle);
        when(articleMapper.toResponseVm(updatedArticle)).thenReturn(responseVm);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/articles/{id}", articleId)
                        .file(imageFile)
                        .param("content", "Updated article content")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(articleId.toString())))
                .andExpect(jsonPath("$.content", is("Updated article content")))
                .andExpect(jsonPath("$.imageUrl", is("http://localhost:8080/api/v1/files/updated-image.jpg")));
    }

    @Test
    public void delete_shouldReturnNoContent_whenArticleExists() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/{id}", articleId))
                .andExpect(status().isNoContent());
    }

}