package com.example.oauth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private String SECRET_KEY = "123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/login")) {
            filterChain.doFilter(request,response);
        } else {
            String header = request.getHeader(AUTHORIZATION);
            if (header != null && header.startsWith("Bearer "))
                try {
                    String token = header.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String []cleimRole = decodedJWT.getClaim("role").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(cleimRole).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    //String cleimRole = String.valueOf(decodedJWT.getClaim("role"));
                    //String cleimRole2 = cleimRole.replace("[", "");
                    //String role = cleimRole2.replace("]", "");
                    //Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));
                   // simpleGrantedAuthorities.stream().collect(Collectors.toList());

                    /*Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
                            .parseClaimsJws(header.replace("Bearer ",""));

                    String username = claimsJws.getBody().get("sub").toString();
                    String cleimRole = claimsJws.getBody().get("role").toString();
                    String role = cleimRole.replace("[", "");
                    String role2= role.replace("]","");
                    Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role2));
                    simpleGrantedAuthorities.stream().collect(Collectors.toList());*/


                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request,response);
                } catch (Exception ex) {
                    logger.error("Error loggin in: {}",new Throwable(ex.getMessage()));
                    response.setHeader("error", ex.getMessage());
                    Map<String, String> error = new HashMap<>();
                    error.put("error message", ex.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
                else {
                    filterChain.doFilter(request,response);
                }

        }
    }
}

