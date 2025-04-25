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
    long id ;

    @Column(nullable = false)
    String userName;

    @Column(nullable = false,unique = true)
    String userEmail;

    @Column(nullable = false)
    String password;;

}
