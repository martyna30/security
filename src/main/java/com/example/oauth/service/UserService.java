package com.example.oauth.service;

import com.example.oauth.AppUser;
import com.example.oauth.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {



    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsernameAdnPassword(String username, String password) throws UsernameNotFoundException {

        Optional<AppUser> appUser = userRepository.findByUsernameAndPassword(username, password);
        return appUser.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with such name doesn't exist"));

    }

    public UserDetails saveUser(AppUser appUser) {
       userRepository.save(appUser);
       return new MyUserDetails(appUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = userRepository.findByUsername(username);
        return appUser.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with such name doesn't exist"));
    }

}

