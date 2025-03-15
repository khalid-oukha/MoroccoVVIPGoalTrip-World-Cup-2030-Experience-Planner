package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponseVm {
    private UUID id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
