package com.moroccanvviptrip.api.mvtapi.web.dto.article;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Trimmed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 50, max = 5000)
    private String content;

    private MultipartFile image;

    private UUID activityId;

}
