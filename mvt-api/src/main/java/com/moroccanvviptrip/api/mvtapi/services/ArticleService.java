package com.moroccanvviptrip.api.mvtapi.services;


import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ArticleService {

    Article findById(UUID id);

    Page<Article> findAll(Pageable pageable);

    Article create(ArticleRequestDto articleRequestDto);

    Article update(UUID id, ArticleUpdateDto articleUpdateDto);

    void delete(UUID id);


}
