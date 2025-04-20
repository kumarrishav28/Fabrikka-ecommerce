package com.notification.notificationService.controller;

import com.notification.notificationService.dto.NotificationDetailsDto;
import com.notification.notificationService.dto.NotificationTempDto;
import com.notification.notificationService.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sendEmail")
public class NotificationController {

    @Autowired
    NotificationService notificationService;


    @PostMapping("/createTemplate")
    ResponseEntity<String> createNotificationTemplate(@RequestBody NotificationTempDto notificationTempDto) {
        notificationService.createNotificationTemplate(notificationTempDto);
        return ResponseEntity.ok("Template created successfully");
    }

    @PostMapping("/sendNotification")
    ResponseEntity<String> sendNotification(@RequestBody NotificationDetailsDto notificationDetailsDto) throws MessagingException {
        notificationService.sendNotification(notificationDetailsDto);
        return ResponseEntity.ok("Notification sent successfully");
    }
}
