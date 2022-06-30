package com.example.oauth;

import com.example.oauth.mapper.AppUserMapper;
import com.example.oauth.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.oauth.service.JwtService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

@RestController
public class HelloApi {

    private String SECRET_KEY = "123";
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Autowired
    UserService userService;


 @Autowired
    AppUserMapper appUserMapper;


    @PostMapping("/auth")
    public String getToken(@RequestBody AppUser appUser) throws Exception {


        /*//sprawdz czy istnieje w bazie user jesli tak to generujesz token
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUserDto.getUsername(), appUserDto.getPassword().getBytes()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(appUserDto.getUsername());

        String jwt = jwtService.generateToken(userDetails);
        //return  ResponseEntity.ok(jwt);
        return jwt;*/

        long currentTimeMillis = System.currentTimeMillis();

       // try {

            UserDetails userInDatabase = userService.loadUserByUsername(appUser.getUsername());
            //if (userInDatabase.getPassword() == appUser.getPassword()) {

               String jwt = Jwts.builder()
                        .setSubject(appUser.setUsername(userInDatabase.getUsername()))
                        .claim("role", appUser.setRole(String.valueOf(userInDatabase.getAuthorities().stream().findFirst())))
                        .setIssuedAt(new Date(currentTimeMillis))
                        .setExpiration(new Date(currentTimeMillis + 2000000))
                        .signWith(signatureAlgorithm, SECRET_KEY.getBytes())
                        .compact();
                return jwt;


       // } catch (BadCredentialsException e) {
           // throw new Exception("Incorrect password", e);
       // }

    }

    @GetMapping("/hello")
    public String login() {
        return "json";
    }

    @GetMapping("/test1")
    public String test1() {
        return "t1";
    }

    @GetMapping("/test2")
    public String test2() {
        return "t2";
    }

    @GetMapping("/test3")
    public String test3() {
        return "t3";
    }

}
