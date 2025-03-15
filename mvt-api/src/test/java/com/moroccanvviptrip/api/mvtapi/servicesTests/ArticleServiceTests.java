package com.moroccanvviptrip.api.mvtapi.servicesTests;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.repository.ArticleRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.impl.ArticleServiceImpl;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ArticleMapper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArticleServiceTests {

    private ArticleRepository articleRepository;
    private ActivityService activityService;
    private ArticleMapper articleMapper;
    private FileStorageService fileStorageService;
    private ArticleServiceImpl articleService;

    private final UUID articleId = UUID.randomUUID();
    private final UUID activityId = UUID.randomUUID();

    @BeforeEach
    public void beforeEach() {
        articleRepository = mock(ArticleRepository.class);
        activityService = mock(ActivityService.class);
        articleMapper = mock(ArticleMapper.class);
        fileStorageService = mock(FileStorageService.class);

        articleService = new ArticleServiceImpl(
                articleRepository,
                activityService,
                articleMapper,
                fileStorageService
        );
    }

    @Test
    public void findById_shouldReturnArticle_whenArticleExists() {
        // Arrange
        Article expectedArticle = createArticle();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(expectedArticle));

        // Act
        Article result = articleService.findById(articleId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedArticle.getId(), result.getId());
        assertEquals(expectedArticle.getContent(), result.getContent());
        assertEquals(expectedArticle.getImageUrl(), result.getImageUrl());
    }

    @Test
    public void findById_shouldThrowEntityNotFoundException_whenArticleDoesNotExist() {
        // Arrange
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> articleService.findById(articleId)
        );
        assertEquals("Article not found", exception.getMessage());
    }

    @Test
    public void findAll_shouldReturnPageOfArticles() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articles = List.of(createArticle(), createArticle());
        Page<Article> expectedPage = new PageImpl<>(articles, pageable, articles.size());

        when(articleRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Article> result = articleService.findAll(pageable);

        // Assert
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent(), result.getContent());
    }

    @Test
    public void create_shouldCreateArticle_whenValidRequest() {
        // Arrange
        Activity activity = Activity.builder()
                .id(activityId)
                .name("Desert Safari")
                .build();

        MultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        ArticleRequestDto requestDto = ArticleRequestDto.builder()
                .content("This is a detailed article about desert safaris in Morocco.")
                .image(imageFile)
                .activityId(activityId)
                .build();

        Article unsavedArticle = new Article();
        unsavedArticle.setContent("This is a detailed article about desert safaris in Morocco.");

        Article savedArticle = new Article();
        savedArticle.setId(articleId);
        savedArticle.setContent("This is a detailed article about desert safaris in Morocco.");
        savedArticle.setImageUrl("http://localhost:8080/api/v1/files/test-image.jpg");
        savedArticle.setActivity(activity);
        savedArticle.setCreatedAt(LocalDateTime.now());

        when(activityService.findById(activityId)).thenReturn(activity);
        when(fileStorageService.store(imageFile)).thenReturn("test-image.jpg");
        when(articleMapper.toEntity(requestDto)).thenReturn(unsavedArticle);
        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        // Act
        Article result = articleService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(articleId, result.getId());
        assertEquals("This is a detailed article about desert safaris in Morocco.", result.getContent());
        assertEquals("http://localhost:8080/api/v1/files/test-image.jpg", result.getImageUrl());
        assertEquals(activity, result.getActivity());
    }

    @Test
    public void update_shouldUpdateArticle_whenArticleExists() {
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setContent("Original content");
        existingArticle.setImageUrl("http://localhost:8080/api/v1/files/old-image.jpg");

        MultipartFile newImageFile = new MockMultipartFile(
                "newImageFile",
                "new-image.jpg",
                "image/jpeg",
                "new image content".getBytes()
        );

        ArticleUpdateDto updateDto = ArticleUpdateDto.builder()
                .content("Updated content")
                .image(newImageFile)
                .build();

        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setContent("Updated content");
        updatedArticle.setImageUrl("http://localhost:8080/api/v1/files/new-image.jpg");

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(fileStorageService.store(newImageFile)).thenReturn("new-image.jpg");
        when(articleMapper.partialUpdate(updateDto, existingArticle)).thenReturn(existingArticle);
        when(articleRepository.save(any(Article.class))).thenReturn(updatedArticle);

        // Act
        Article result = articleService.update(articleId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(articleId, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals("http://localhost:8080/api/v1/files/new-image.jpg", result.getImageUrl());
    }

    @Test
    public void update_shouldUpdateContentOnly_whenNoNewImage() {
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setContent("Original content");
        existingArticle.setImageUrl("http://localhost:8080/api/v1/files/image.jpg");

        ArticleUpdateDto updateDto = ArticleUpdateDto.builder()
                .content("Updated content")
                .image(null)
                .build();

        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setContent("Updated content");
        updatedArticle.setImageUrl("http://localhost:8080/api/v1/files/image.jpg");

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(articleMapper.partialUpdate(updateDto, existingArticle)).thenReturn(existingArticle);
        when(articleRepository.save(any(Article.class))).thenReturn(updatedArticle);

        // Act
        Article result = articleService.update(articleId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(articleId, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals("http://localhost:8080/api/v1/files/image.jpg", result.getImageUrl());
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenArticleDoesNotExist() {
        // Arrange
        ArticleUpdateDto updateDto = ArticleUpdateDto.builder()
                .content("Updated content")
                .build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> articleService.update(articleId, updateDto)
        );
        assertEquals("Article not found", exception.getMessage());
    }

    @Test
    public void delete_shouldDeleteArticle_whenArticleExists() {
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setContent("Article content");
        existingArticle.setImageUrl("http://localhost:8080/api/v1/files/image.jpg");

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

        // Act
        articleService.delete(articleId);

        // Assert
        verify(fileStorageService).delete("image.jpg");
        verify(articleRepository).deleteById(articleId);
    }

    @Test
    public void delete_shouldThrowEntityNotFoundException_whenArticleDoesNotExist() {
        // Arrange
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> articleService.delete(articleId)
        );
        assertEquals("Article not found", exception.getMessage());
    }

    // Helper method to create a test article
    private Article createArticle() {
        Activity activity = Activity.builder()
                .id(activityId)
                .name("Desert Safari")
                .build();

        Article article = new Article();
        article.setId(articleId);
        article.setContent("This is a detailed article about desert safaris in Morocco.");
        article.setImageUrl("http://localhost:8080/api/v1/files/test-image.jpg");
        article.setActivity(activity);
        article.setCreatedAt(LocalDateTime.now());

        return article;
    }
}