package com.notification.notificationService.service.impl;


import com.notification.notificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.fabrikka.common.NotificationTempDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TemplateInitializer implements org.springframework.boot.CommandLineRunner {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void run(String... args) {
        NotificationTempDto notificationTempDto = new NotificationTempDto();
        notificationTempDto.setTemplateName("welcome");
        notificationTempDto.setSubject("Welcome to our Fabrikka!");
        notificationTempDto.setDynamicFields("subject,user");
        notificationService.createNotificationTemplate(notificationTempDto);
    }
}
