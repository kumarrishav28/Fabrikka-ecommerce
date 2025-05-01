package com.fabrikka.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationTempDto {

    private String templateName;
    private String subject;
    private String dynamicFields;


}
