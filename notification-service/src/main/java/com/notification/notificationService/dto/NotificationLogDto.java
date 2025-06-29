package com.notification.notificationService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationLogDto {

    public long notifLogId;
    public String notifTempName;
    public String logException;
    public String status ;
    public Date excpetionDate;

}