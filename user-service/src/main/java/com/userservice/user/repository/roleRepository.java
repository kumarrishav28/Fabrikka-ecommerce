package com.userservice.user.repository;

import com.userservice.user.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface roleRepository extends JpaRepository<Roles, Long> {

    Roles findByName(String roleName);

}
