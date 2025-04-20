package com.userservice.user.controller;

import com.userservice.user.dto.UserDto;
import com.userservice.user.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    userService userService;

    @PostMapping("/create")
    ResponseEntity<String> createUser(@RequestBody UserDto user) {
        userService.createUser(user);
        return ResponseEntity.ok("User Created");
    }

    @GetMapping("/find/{email}")
    ResponseEntity<UserDto> findUserByEmail(@PathVariable  String email) {
        UserDto userDto = userService.findUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }


}
