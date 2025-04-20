package com.userservice.user.service;

import com.userservice.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface userService {

    public void createUser(UserDto userDto);

    public UserDto findUserByEmail(String email);
}
