package com.moroccanvviptrip.api.mvtapi.web.dto.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {
    private String content;
    private String imageUrl;
    private UUID activityId;
}
