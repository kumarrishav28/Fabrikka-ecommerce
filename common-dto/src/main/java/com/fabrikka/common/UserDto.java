package com.fabrikka.common;

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
    private String firstName;
    private String lastName;
    private String userEmail;
    private String password;


}
