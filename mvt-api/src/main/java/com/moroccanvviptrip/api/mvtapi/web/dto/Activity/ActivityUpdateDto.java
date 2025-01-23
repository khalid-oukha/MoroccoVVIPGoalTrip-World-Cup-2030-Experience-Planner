package com.moroccanvviptrip.api.mvtapi.web.dto.Activity;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class ActivityUpdateDto {
    private String name;
    private String description;
    private String location;
    private Boolean available;
    private UUID categoryId;
    private UUID cityId;
    private List<MultipartFile> images;
}
