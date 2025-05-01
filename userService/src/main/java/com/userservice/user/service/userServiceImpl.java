package com.userservice.user.service;

import com.fabrikka.common.UserDto;
import com.userservice.user.entity.Roles;
import com.userservice.user.entity.User;
import com.userservice.user.repository.roleRepository;
import com.userservice.user.repository.userRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class userServiceImpl implements userService {

    private final userRepository userRepository;
    private final roleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public userServiceImpl(userRepository userRepository, roleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserDto userDto) {
        // Convert UserDto to User entity
        User user = new User();
        user.setUserEmail(userDto.getUserEmail());
        user.setUserName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Roles roles = roleRepository.findByName("ROLE_ADMIN");
        if (roles == null) {
            Roles role = new Roles();
            role.setName("ROLE_ADMIN");
            roles = roleRepository.save(role);
        }
        user.setRoles(Collections.singletonList(roles));

        // Save the user entity to the database
        return userRepository.save(user);

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByUserEmail(email).orElse(null);

    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        UserDto[] userDtos = new UserDto[users.size()];
        for (int i = 0; i < users.size(); i++) {
            userDtos[i] = new UserDto();
            setUserDto(userDtos[i], users.get(i));
        }
        return Arrays.asList(userDtos);
    }

    private void setUserDto(UserDto userDto, User user) {
        userDto.setUserEmail(user.getUserEmail());
        userDto.setFirstName(user.getUserName().split(" ")[0]);
        userDto.setLastName(user.getUserName().split(" ")[1]);
    }
}
