package com.moroccanvviptrip.api.mvtapi.web.VM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseVm {
    private UUID id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
