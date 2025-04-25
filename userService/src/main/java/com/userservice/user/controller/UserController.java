package com.userservice.user.controller;

import com.userservice.user.dto.NotificationDetailsDto;
import com.userservice.user.dto.UserDto;
import com.userservice.user.entity.User;
import com.userservice.user.service.userService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final userService userService;

    private final RestTemplate restTemplate;

    public UserController(userService userService) {

        this.userService = userService;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("index")
    public String home (){
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/createUser")
    public String registerUser(@Valid @ModelAttribute("user") UserDto user,BindingResult bindingResult,Model model) {

        User existingUser = userService.findUserByEmail(user.getUserEmail());
        if (existingUser != null) {
            bindingResult.rejectValue("userEmail", null, "There is already an account registered with the email provided");
        }
        User newUser = userService.createUser(user);

        CompletableFuture<Void> emailFuture = CompletableFuture.runAsync(() -> {
            sendEmail(newUser);
        });

        if(bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        return "redirect:/register?success";

    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    private void sendEmail(User user) {
        NotificationDetailsDto notification = new NotificationDetailsDto();
        try {
            notification.setUserId(String.valueOf(user.getId()));
            notification.setTemplateName("welcome");
            notification.setToList(List.of(user.getUserEmail()));
            notification.setCcList(new ArrayList<>());
            String message = restTemplate.postForObject("http://localhost:8082/sendEmail/sendNotification", notification, String.class);
            logger.info("Email sent successfully: {}", message);
        } catch (Exception e) {
            logger.warn("Error occurred while sending email: {}", e.getMessage());
        }

    }


}
