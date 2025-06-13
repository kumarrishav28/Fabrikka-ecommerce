package com.fabrikka.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDetailsDto {

    Map<String,String> toUserDetails;

    Map<String,String> ccUserDetails;

    public String templateName;

    public boolean sendAttachment;

    Map<String,String> batchNotificationAttributes = new HashMap<>();

}
