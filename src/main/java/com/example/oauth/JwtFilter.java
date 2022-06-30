package com.example.oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Collections;
import java.util.Set;

import static javax.crypto.Cipher.SECRET_KEY;


public class JwtFilter extends BasicAuthenticationFilter {
    private String SECRET_KEY = "123";

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthenticationByToken(header);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);





































            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(header.replace("Bearer ",""));

        String username = claimsJws.getBody().get("sub").toString();
        String cleimRole = claimsJws.getBody().get("role").toString();
        String role = cleimRole.replace("[", "");
        String role2= role.replace("]","");
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role2));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, SECRET_KEY.getBytes(), simpleGrantedAuthorities);
        return usernamePasswordAuthenticationToken;

    }
}




       /* HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String header = httpServletRequest.getHeader("Authorization");

        if(httpServletRequest == null || !header.startsWith("Bearer ")) {
            throw new ServerException("Wrong or empty header");
        } else {
            try {
                String token = header.substring(7);

                Claims claims = Jwts.parser().setSigningKey("aaa".getBytes()).parseClaimsJws(token).getBody();
                httpServletRequest.setAttribute("claims", claims);
            } catch (Exception e) {
                throw new ServerException("wrong key");
            }
        }
        chain.doFilter(httpServletRequest, response);
    }






        /*Jws<Claims> claimsJws = Jwts.parser().parseClaimsJws(header.replace("Bearer ", ""));

        Jwts.parser().setSigningKey("123".getBytes());
        String username = claimsJws.getBody().get("username").toString();
        String role = claimsJws.getBody().get("role").toString();
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
        return usernamePasswordAuthenticationToken;
    }*/
//}
