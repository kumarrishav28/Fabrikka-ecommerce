package com.notification.notificationService.service;

import com.fabrikka.common.NotificationDetailsDto;
import com.fabrikka.common.NotificationTempDto;
import jakarta.mail.MessagingException;

import java.util.List;

public interface NotificationService {


    public void createNotificationTemplate(NotificationTempDto notificationTempDto);

    public void sendNotification(NotificationDetailsDto notificationDetailsDto) throws MessagingException;

    public void sendNotificationGeneric(NotificationDetailsDto notificationDetailsDto) throws MessagingException;
}
