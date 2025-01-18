package com.moroccanvviptrip.api.mvtapi.web.exception;

public class InsufficientTokensException extends RuntimeException {
    public InsufficientTokensException(String s) {
        super(s);
    }

    public InsufficientTokensException(String message, Throwable cause) {
        super(message, cause);
    }
}
