package com.moroccanvviptrip.api.mvtapi.aop;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

@ControllerAdvice
public class GlobalControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor());
    }

    private static class StringTrimmerEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) {
            if (text == null) {
                setValue(null);
            } else {
                setValue(text.trim());
            }
        }
    }
}
