package com.notification.notificationService.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message , String userid) {
        super(message);
    }

}
