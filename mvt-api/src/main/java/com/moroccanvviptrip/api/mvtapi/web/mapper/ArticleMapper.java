package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.web.VM.ArticleResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(ArticleRequestDto articleRequestDto);

    ArticleResponseVm toResponseVm(Article article);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "articleUpdateDto.content", target = "content")
    Article partialUpdate(ArticleUpdateDto articleUpdateDto, Article article);
}
