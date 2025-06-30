package com.userservice.user.config;

import com.fabrikka.common.CreateOrderRequest;
import com.fabrikka.common.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "order-service", url = "http://api-gateway:8084")
public interface OrderClient {

    @PostMapping("/orders/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request);

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId);

    @PostMapping("/orders/remove/{userId}")
    public ResponseEntity<String> removeOrder(@PathVariable Long userId);
}
