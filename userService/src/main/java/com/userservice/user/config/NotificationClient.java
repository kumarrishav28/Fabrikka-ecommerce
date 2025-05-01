package com.userservice.user.config;

import com.fabrikka.common.NotificationDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8082")
public interface NotificationClient {


    @PostMapping("/sendEmail/sendNotification")
    ResponseEntity<String> sendMail(@RequestBody NotificationDetailsDto notification) ;

}
