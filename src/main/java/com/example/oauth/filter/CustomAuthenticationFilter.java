package com.example.oauth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.oauth.AppUser;

import com.example.oauth.MyUserDetails;
import com.example.oauth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String SECRET_KEY = "123";
    @Autowired
    UserService userService;
    //SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager= authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       String username = request.getParameter("username");
       String password = request.getParameter("password");
       log.info("username:",username); log.info("password:", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
       MyUserDetails userIn =  (MyUserDetails) authentication.getPrincipal();

       //UserDetails userInDatabase = userService.loadUserByUsername(appUser.getUsername());

        long currentTimeMillis = System.currentTimeMillis();

        String acess_token = JWT.create()
                .withSubject(userIn.getUsername())
                .withExpiresAt(new Date(currentTimeMillis + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", userIn.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);



        /*String acess_token = Jwts.builder()
                .setSubject(userIn.getUsername())
                .claim("role", userIn.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 2000000))
                .signWith(signatureAlgorithm, SECRET_KEY.getBytes())
                .compact();

        /*String refresh_token = Jwts.builder()
                .setSubject(userInDatabase.getUsername())
                .claim("role", userInDatabase.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 30 * 60 * 1000))
                .signWith(signatureAlgorithm, SECRET_KEY.getBytes())
                .compact();
        */
        response.setHeader("acess_token", acess_token);
        //response.setHeader("refresh_token", refresh_token);

        Map<String, String> tokens =  new HashMap<>();
        tokens.put("access_token",acess_token);
        //tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }
}

    /*String acess_token = Jwts.builder()
            .setSubject(userIn.getUsername())
            .claim("role",
                    Arrays.stream(userIn.getRole().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
//userIn.getRole().split().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(currentTimeMillis))
                        .setExpiration(new Date(currentTimeMillis + 10 * 60 * 1000))
                        .signWith(signatureAlgorithm, SECRET_KEY.getBytes())
                        .compact();*/
