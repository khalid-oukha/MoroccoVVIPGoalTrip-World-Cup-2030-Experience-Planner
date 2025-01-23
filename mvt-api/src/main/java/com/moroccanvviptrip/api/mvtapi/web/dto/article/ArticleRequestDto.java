package com.moroccanvviptrip.api.mvtapi.web.dto.article;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {
    @NotBlank
    @Trimmed
    private String content;

    private MultipartFile image;

    @NotBlank
    private UUID activityId;
}
