package com.userservice.user.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "load-product-service", url = "http://localhost:9191")
public interface LoadProductClient {

    @PostMapping(value = "/productFile/uploadProductFile",consumes = "multipart/form-data")
    void uploadProductFile(@RequestPart("file") MultipartFile file) ;

}
