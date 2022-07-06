package com.example.oauth;

import com.example.oauth.filter.CustomAuthenticationFilter;
import com.example.oauth.filter.CustomAuthorizationFilter;
import com.example.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;


    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =  new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable(); //postman
        http.headers().disable();//h2
        //zmina permit autenty potem role
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/test3").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/test1").authenticated();
        //http.addFilter(new JwtFilter(authenticationManager()));
         http.addFilter(customAuthenticationFilter);
        //http.addFilterBefore(new JwtFilter(),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);



               /*http.authorizeRequests().antMatchers("/auth").permitAll().and()
                       .authorizeRequests().antMatchers("/test2").authenticated();
                       //.and().authorizeRequests().antMatchers("/test3").hasRole("ADMIN");
               //wymaga autoryzacji czyli sprawdzonego json
                http.addFilter(new JwtFilter(authenticationManager()));
                .addFilterAfter(new JwtFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);

     //http.addFilterBefore(new JwtFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class
     // http .addFilterAfter(new JwtFilter(authenticationManager()), BasicAuthenticationFilter.class);
                */
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(User.builder()
                        .username("Piotr")
                                .password(passwordEncoder().encode("test"))
                                        .roles("ADMIN"));

    }*/

    }








