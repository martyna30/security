package com.example.oauth.mapper;

import com.example.oauth.AppUser;
import com.example.oauth.AppUserDto;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUser mapToAppUser(AppUserDto appUserDto) {
        return new AppUser(
                appUserDto.getId(),
                appUserDto.getPassword(),
                appUserDto.getUsername(),
                appUserDto.getRole()
        );
    }

    public AppUserDto mapToAppUserDto(AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getPassword(),
                appUser.getUsername(),
                appUser.getRole()
        );

    }
}
