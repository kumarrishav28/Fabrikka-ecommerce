package com.notification.notificationService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "userDetails")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    long userId ;

    @Column(name = "user_name")
    String userName;
    
    @Column(name = "user_email")
    String userEmail;

    @Column(name = "user_phone")
    String userPhone;

    @Column(name = "user_address")
    String userAddress;
}
