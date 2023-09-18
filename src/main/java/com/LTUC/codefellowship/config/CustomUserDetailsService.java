package com.LTUC.codefellowship.config;

import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.repositories.ApplicationUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

        if (applicationUser == null){
            System.out.println(username + " , Sorry User not found");
            throw new UsernameNotFoundException("user" + username + " sorry, this user was not found in te database");
        }

        System.out.println("Found the user: " + applicationUser.getUsername());
        return applicationUser;
    }
}
