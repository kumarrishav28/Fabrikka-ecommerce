package com.userservice.user.service;

import com.userservice.user.dto.UserDto;
import com.userservice.user.entity.User;
import com.userservice.user.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    @Autowired
    userRepository userRepository;

    @Override
    public void createUser(UserDto userDto) {
        // Convert UserDto to User entity
        User user = new User();
        user.setUserEmail(userDto.getUserEmail());
        user.setUserName(userDto.getUserName());
        user.setUserPhone(userDto.getUserPhone());
        user.setUserAddress(userDto.getUserAddress());
        // Save the user entity to the database
        userRepository.save(user);

    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert User entity to UserDto
        UserDto userDto = new UserDto();
        userDto.setUserEmail(user.getUserEmail());
        userDto.setUserName(user.getUserName());
        userDto.setUserPhone(user.getUserPhone());
        userDto.setUserAddress(user.getUserAddress());
        return userDto;

    }
}
