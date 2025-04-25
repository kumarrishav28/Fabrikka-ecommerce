package com.userservice.user.controller;

import com.userservice.user.dto.UserDto;
import com.userservice.user.entity.User;
import com.userservice.user.service.userService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final userService userService;

    public UserController(userService userService) {
        this.userService = userService;
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
        userService.createUser(user);

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


}
