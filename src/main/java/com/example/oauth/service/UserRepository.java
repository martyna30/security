package com.example.oauth.service;


import com.example.oauth.AppUser;
import com.example.oauth.MyUserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameAndPassword(String username, String password);

    @Override
    AppUser save(AppUser appUser);


    Optional<AppUser> findByUsername(String username);

}
