package com.example.oauth.service;

import com.example.oauth.AppUser;
import com.example.oauth.MyUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
public class Start {

    public Start( PasswordEncoder passwordEncoder, UserService userService) {

        AppUser appUser = new AppUser();
        appUser.setUsername("basia");
        appUser.setPassword(passwordEncoder.encode("123"));
        appUser.setRole("ROLE_ADMIN");
        userService.saveUser(appUser);

    }

}
