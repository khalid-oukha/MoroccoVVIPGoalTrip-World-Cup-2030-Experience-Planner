package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.Article;
import com.moroccanvviptrip.api.mvtapi.web.VM.ActivityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.article.ArticleUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(ArticleRequestDto articleRequestDto);

    ActivityResponseVm toResponseVm(Article article);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Article partialUpdate(ArticleUpdateDto articleUpdateDto, Article article);
}
