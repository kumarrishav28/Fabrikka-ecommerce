package com.notification.notificationService.service;

import com.notification.notificationService.dto.NotificationDetailsDto;
import com.notification.notificationService.dto.NotificationTempDto;
import com.notification.notificationService.entity.NotificationTemplate;
import jakarta.mail.MessagingException;

import java.util.List;

public interface NotificationService {


    public void createNotificationTemplate(NotificationTempDto notificationTempDto);

    public void sendEmail(String UserId , String templateName, List<String> toCcList) throws MessagingException;

    public void sendNotification(NotificationDetailsDto notificationDetailsDto) throws MessagingException;
}
