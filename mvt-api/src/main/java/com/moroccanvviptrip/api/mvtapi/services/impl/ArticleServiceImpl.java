package com.moroccanvviptrip.api.mvtapi.services.impl;


import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.repository.ArticleRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.ArticleService;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ArticleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ActivityService activityService;
    private final ArticleMapper articleMapper;
    private final FileStorageService fileStorageService;

    @Override
    public Article findById(UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
    }

    @Override
    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Article create(ArticleRequestDto articleRequestDto) {
        Activity activity = activityService.findById(articleRequestDto.getActivityId());
        String fileName = fileStorageService.store(articleRequestDto.getImage());
        String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;

        Article article = articleMapper.toEntity(articleRequestDto);
        article.setImageUrl(imageUrl);
        article.setActivity(activity);
        return articleRepository.save(article);
    }

    @Override
    public Article update(UUID id, ArticleUpdateDto articleUpdateDto) {
        Article exisitingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if (articleUpdateDto.getImage() != null && !articleUpdateDto.getImage().isEmpty()) {
            if (exisitingArticle.getImageUrl() != null && !exisitingArticle.getImageUrl().isEmpty()) {
                String oldFileName = exisitingArticle.getImageUrl()
                        .substring(exisitingArticle.getImageUrl().lastIndexOf("/") + 1);
                fileStorageService.delete(oldFileName);
            }

            String fileName = fileStorageService.store(articleUpdateDto.getImage());
            String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;
            exisitingArticle.setImageUrl(imageUrl);
        }

        Article article = articleMapper.partialUpdate(articleUpdateDto, exisitingArticle);

        return articleRepository.save(article);
    }

    @Override
    public void delete(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            String fileName = article.getImageUrl()
                    .substring(article.getImageUrl().lastIndexOf("/") + 1);
            fileStorageService.delete(fileName);
        }
        articleRepository.deleteById(id);
    }
}
