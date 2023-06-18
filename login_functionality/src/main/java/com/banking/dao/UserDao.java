package com.banking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.model.Roles;
import com.banking.model.User;

@SuppressWarnings("unused")
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
    List<User> findByRolesContaining(String role);


}