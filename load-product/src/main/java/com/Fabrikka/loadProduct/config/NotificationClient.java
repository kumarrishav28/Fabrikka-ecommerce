package com.Fabrikka.loadProduct.config;

import com.fabrikka.common.NotificationDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8084")
public interface NotificationClient {


    @PostMapping("/sendEmail/sendNotificationGeneric")
    void sendNotificationGeneric(@RequestBody NotificationDetailsDto notification);

}
