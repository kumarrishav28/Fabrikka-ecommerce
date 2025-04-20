package com.userservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAddress;


}
