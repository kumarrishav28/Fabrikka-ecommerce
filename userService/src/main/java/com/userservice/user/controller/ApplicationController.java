package com.userservice.user.controller;

import com.fabrikka.common.AddItemRequest;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.NotificationDetailsDto;
import com.fabrikka.common.UserDto;
import com.userservice.user.config.CartClient;
import com.userservice.user.config.NotificationClient;
import com.userservice.user.entity.User;
import com.userservice.user.service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private static final ConcurrentHashMap<String, Object> userCache = new ConcurrentHashMap<>();

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final userService userService;

    private final RestTemplate restTemplate;

    private final CartClient cartClient;

    final NotificationClient notificationClient;

    @GetMapping("index")
    public String home() {
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
    public String registerUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model) {

        User existingUser = userService.findUserByEmail(user.getUserEmail());
        if (existingUser != null) {
            bindingResult.rejectValue("userEmail", null, "There is already an account registered with the email provided");
        }
        User newUser = userService.createUser(user);
        cacheUser("user", newUser);

        CompletableFuture<Void> emailFuture = CompletableFuture.runAsync(() -> {
            sendEmail(new ArrayList<>(List.of(newUser)), new ArrayList<>());
        });

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        return "redirect:/index";

    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    private void sendEmail(List<User> toUsers, List<User> ccUsers) {
        NotificationDetailsDto notification = new NotificationDetailsDto();
        try {
            notification.setTemplateName("welcome");
            notification.setToUserDetails(createReceiversMap(toUsers));
            notification.setCcUserDetails(createReceiversMap(ccUsers));
            //String message = restTemplate.postForObject("http://localhost:8082/sendEmail/sendNotification", notification, String.class);
            ResponseEntity<String> message = notificationClient.sendMail(notification);
            logger.info("Email sent successfully: {}", message.getBody());
        } catch (Exception e) {
            logger.warn("Error occurred while sending email: {}", e.getMessage());
        }

    }

    private Map<String, String> createReceiversMap(List<User> users) {

        Map<String, String> receiversMap = new HashMap<>();
        if (users == null || users.isEmpty()) {
            return receiversMap;
        }
        for (User user : users) {
            receiversMap.put(user.getUserEmail(), user.getUserName());
        }
        return receiversMap;
    }


    @GetMapping("/cart")
    public String cart(Model model) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null;
        ResponseEntity<CartDto> response = cartClient.getCart(userId);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("cart", response.getBody());
        } else {
            model.addAttribute("cart", new CartDto()); // Empty cart if no data is found
        }

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("productId") Long productId, @ModelAttribute("quantity") int quantity) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null;
        AddItemRequest request = new AddItemRequest(productId, quantity);
        cartClient.addItemToCart(userId, request);
        return "redirect:/index";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@ModelAttribute("itemId") Long itemId) {
        User cachedUser = (User) getCachedUser("user");
        Long userId = cachedUser != null ? cachedUser.getId() : null; // Replace with actual logged-in user ID
        cartClient.removeItemFromCart(userId, itemId);
        return "redirect:/cart";
    }

    public static void cacheUser(String username, Object userDetails) {
        userCache.put(username, userDetails);
    }

    public static Object getCachedUser(String username) {
        return userCache.get(username);
    }

    public static void removeCachedUser(String username) {
        userCache.remove(username);
    }


}
