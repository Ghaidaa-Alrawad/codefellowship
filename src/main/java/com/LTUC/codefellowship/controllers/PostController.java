package com.LTUC.codefellowship.controllers;


import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.models.Post;
import com.LTUC.codefellowship.repositories.ApplicationUserRepo;
import com.LTUC.codefellowship.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    PostRepo postRepo;
    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @GetMapping("/myprofile")
    public String getUserProfilePage(Principal p, Model m){
        if (p != null){
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

            List<Post> userPosts = postRepo.findByUserId(applicationUser);

            m.addAttribute("username", username);
            m.addAttribute("firstName", applicationUser.getFirstName());
            m.addAttribute("lastName", applicationUser.getLastName());
            m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
            m.addAttribute("bio", applicationUser.getBio());

            m.addAttribute("userPosts", userPosts);

            // Add the path to the default profile picture
            m.addAttribute("defaultProfilePicture", "https://i.pinimg.com/236x/73/8b/82/738b82ae3c1a1b793aa9a68d9b19439f.jpg");
        }
        return "userProfile.html";
    }



    @PostMapping("/post/create")
    public RedirectView createPost(Principal principal, @RequestParam String body) {
        if (principal != null) {
            String username = principal.getName();
            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

            // Create a new post
            Post post = new Post(applicationUser, body, LocalDate.now());

            // Save the post to the database
            postRepo.save(post);
        }

        // Redirect back to the user profile page
        return new RedirectView("myprofile");
    }
}
