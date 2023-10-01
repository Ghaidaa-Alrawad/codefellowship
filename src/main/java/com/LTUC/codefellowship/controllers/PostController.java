package com.LTUC.codefellowship.controllers;

import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.models.Post;
import com.LTUC.codefellowship.repositories.ApplicationUserRepo;
import com.LTUC.codefellowship.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
            m.addAttribute("defaultProfilePicture", "https://i.pinimg.com/564x/b6/80/b9/b680b917d8b5e428d6dadca3a15684bb.jpg");
        }
        return "userProfile.html";
    }

    @PostMapping("/post/create")
    public RedirectView createPost(Principal principal, @RequestParam String body) {
        if (principal != null) {
            String username = principal.getName();
            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

            Post post = new Post(applicationUser, body, LocalDate.now());

            postRepo.save(post);
        }

        return new RedirectView("/myprofile");
    }

    @GetMapping("/feed")
    public String getFeedPage(Model model, Principal principal) {
        if (principal != null) {
            ApplicationUser user = applicationUserRepo.findByUsername(principal.getName());

            Set<ApplicationUser> followPeople = user.getFollowing();
            followPeople.remove(user);

            List<Post> followedUsersPosts = postRepo.findByUserIdIn(followPeople);
            model.addAttribute("posts", followedUsersPosts);
        }
        return "feed.html";
    }
}