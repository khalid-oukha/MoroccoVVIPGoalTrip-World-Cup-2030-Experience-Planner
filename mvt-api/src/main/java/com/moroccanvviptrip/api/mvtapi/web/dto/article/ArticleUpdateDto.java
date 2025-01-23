package com.moroccanvviptrip.api.mvtapi.web.dto.article;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import org.springframework.web.multipart.MultipartFile;

public class ArticleUpdateDto {
    @Trimmed
    private String content;
    private MultipartFile image;
}
