package com.example.oauth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class AppUser  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    private String role;


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }

    public String setUsername(String username) {
        this.username = username;
        return username;
    }

    public String setPassword(String password) {
        this.password = password;
        return password;
    }

    public String setRole(String role) {
        this.role = role;
        return role;
    }

    public Long setId(Long id) {
        this.id = id;
        return id;
    }
}
