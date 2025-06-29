package com.userservice.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "userDetails")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id ;

    @Column(nullable = false)
    String userName;

    @Column(nullable = false,unique = true)
    String userEmail;

    @Column(nullable = false)
    String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "ID"))
    List<Roles> roles;
}