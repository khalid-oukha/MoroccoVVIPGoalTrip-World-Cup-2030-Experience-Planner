package com.moroccanvviptrip.api.mvtapi.web.dto.article;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleUpdateDto {
    @Trimmed
    @Size(min = 50, max = 5000)
    private String content;
    private MultipartFile image;
}
