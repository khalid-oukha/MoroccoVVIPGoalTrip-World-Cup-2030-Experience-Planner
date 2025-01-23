package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(ArticleRequestDto articleRequestDto);
}
