package com.userservice.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDetailsDto {

    public List<String> toList;

    public List<String> ccList;

    public String userId;

    public String templateName;

    public boolean sendAttachment;
}
