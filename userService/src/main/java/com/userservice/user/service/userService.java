package com.userservice.user.service;

import com.fabrikka.common.UserDto;
import com.userservice.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface userService {

    public User createUser(UserDto userDto);

    public User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
