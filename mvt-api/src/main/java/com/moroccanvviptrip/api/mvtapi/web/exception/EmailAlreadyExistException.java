package com.moroccanvviptrip.api.mvtapi.web.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("Email already exists");
    }
}
