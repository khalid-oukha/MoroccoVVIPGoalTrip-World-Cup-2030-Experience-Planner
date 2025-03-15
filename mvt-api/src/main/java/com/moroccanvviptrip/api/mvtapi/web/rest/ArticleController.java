package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.services.ArticleService;
import com.moroccanvviptrip.api.mvtapi.web.VM.ArticleResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ArticleMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleMapper articleMapper;
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleResponseVm>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Article> articles = articleService.findAll(pageable);
        return ResponseEntity.ok(articles.map(articleMapper::toResponseVm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseVm> findById(@PathVariable UUID id) {
        Article article = articleService.findById(id);
        return ResponseEntity.ok(articleMapper.toResponseVm(article));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ArticleResponseVm> create(@Valid @ModelAttribute ArticleRequestDto articleRequestDto) {
        Article article = articleService.create(articleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleMapper.toResponseVm(article));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ArticleResponseVm> update(@PathVariable UUID id, @ModelAttribute ArticleUpdateDto articleUpdateDto) {
        Article article = articleService.update(id, articleUpdateDto);
        return ResponseEntity.ok(articleMapper.toResponseVm(article));
    }
}
