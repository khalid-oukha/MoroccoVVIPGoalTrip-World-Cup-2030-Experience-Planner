package com.moroccanvviptrip.api.mvtapi.web.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ApiErrorResponse {
    private final int status;
    private final String message;
    private final Object details;
    private final String timestamp;

    public static ApiErrorResponse create(int status, String message, Object details) {
        return ApiErrorResponse.builder()
                .status(status)
                .message(message)
                .details(details)
                .timestamp(Instant.now().toString())
                .build();
    }
}