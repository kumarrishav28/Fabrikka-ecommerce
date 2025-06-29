package com.notification.notificationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(String message,String templateName) {
        super(message);
    }

}
