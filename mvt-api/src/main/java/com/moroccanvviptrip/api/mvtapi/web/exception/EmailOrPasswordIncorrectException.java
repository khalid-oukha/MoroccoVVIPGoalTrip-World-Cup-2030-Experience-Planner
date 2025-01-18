package com.moroccanvviptrip.api.mvtapi.web.exception;

public class EmailOrPasswordIncorrectException extends RuntimeException {

    public EmailOrPasswordIncorrectException(String message) {
        super(message);
    }
}
