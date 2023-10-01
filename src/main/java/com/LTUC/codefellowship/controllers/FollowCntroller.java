package com.LTUC.codefellowship.controllers;

import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.repositories.ApplicationUserRepo;
import com.LTUC.codefellowship.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class FollowCntroller {

    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @Autowired
    PostRepo postRepo;

    @PostMapping("/follow/{userId}")
    public RedirectView followUser(@PathVariable Long userId, Principal principal) {
        ApplicationUser loggedInUser = applicationUserRepo.findByUsername(principal.getName());

        ApplicationUser userToFollow = applicationUserRepo.findById(userId).orElseThrow();

        if (loggedInUser != null && userToFollow !=null){
            userToFollow.getFollowers().add(loggedInUser);
            applicationUserRepo.save(userToFollow);
        }
        return new RedirectView("/users/"+userId);
    }
}
