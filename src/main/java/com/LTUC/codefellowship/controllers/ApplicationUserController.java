package com.LTUC.codefellowship.controllers;

import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.repositories.ApplicationUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;
import com.LTUC.codefellowship.models.Post;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String getLogInPage(){
        return "login.html";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup.html";
    }

    @GetMapping("/logout")
    public String getLogoutPage(){
        return "index.html";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(username);

        String encryptedPassword = passwordEncoder.encode(password);
        applicationUser.setPassword(encryptedPassword);

        applicationUser.setFirstName(firstName);
        applicationUser.setLastName(lastName);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateOfBirth);
            applicationUser.setDateOfBirth(dateFormat.format(parsedDate));


        } catch (ParseException e) {
            e.printStackTrace();
            return new RedirectView("/signup?error=date");
        }

        applicationUser.setBio(bio);

        applicationUserRepo.save(applicationUser);
        authWithHttpServletRequest(username, password);
        return new RedirectView("/");
    }

    @GetMapping("/")
    public String getHomePage(Principal p, Model m){
        if (p != null){
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", applicationUser.getFirstName());
            m.addAttribute("lastName", applicationUser.getLastName());
            m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
            m.addAttribute("bio", applicationUser.getBio());

            m.addAttribute("loggedInUser", applicationUser);
        }
        return "index.html";
    }

//    @GetMapping("/users/{id}")
//    public String getUserInfoById(Model m, Principal p, @PathVariable Long id) {
//        if (p != null) {
//            String username = p.getName();
//            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);
//
//            m.addAttribute("username", username);
//            m.addAttribute("firstName", applicationUser.getFirstName());
//            m.addAttribute("lastName", applicationUser.getLastName());
//            m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
//            m.addAttribute("bio", applicationUser.getBio());
//            m.addAttribute("defaultProfilePicture", "https://i.pinimg.com/564x/b6/80/b9/b680b917d8b5e428d6dadca3a15684bb.jpg");
//        }
//
//        ApplicationUser applicationUser = applicationUserRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("user not found with id "+id));
//
//        List<Post> userPosts = applicationUser.getPosts();
//
//        m.addAttribute("userPosts", userPosts);
//
//        m.addAttribute("username", applicationUser.getUsername());
//        m.addAttribute("firstName", applicationUser.getFirstName());
//        m.addAttribute("lastName", applicationUser.getLastName());
//        m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
//        m.addAttribute("bio", applicationUser.getBio());
//
//        return "usersById.html";
//    }

    @GetMapping("/users/{id}")
    public String getUserInfoById(Model m, Principal p, @PathVariable Long id) {
        if (p != null) {
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", applicationUser.getFirstName());
            m.addAttribute("lastName", applicationUser.getLastName());
            m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
            m.addAttribute("bio", applicationUser.getBio());
            m.addAttribute("defaultProfilePicture", "https://i.pinimg.com/564x/b6/80/b9/b680b917d8b5e428d6dadca3a15684bb.jpg");

            ApplicationUser userProfile = applicationUserRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

            List<Post> userPosts = userProfile.getPosts();

            boolean isFollowing = applicationUser.getFollowing().contains(userProfile);

            m.addAttribute("userPosts", userPosts);
            m.addAttribute("username", userProfile.getUsername());
            m.addAttribute("firstName", userProfile.getFirstName());
            m.addAttribute("lastName", userProfile.getLastName());
            m.addAttribute("dateOfBirth", userProfile.getDateOfBirth());
            m.addAttribute("bio", userProfile.getBio());
            m.addAttribute("userPage", true);
            m.addAttribute("isFollowing", isFollowing);

            m.addAttribute("userProfileId", id);
        }

        return "usersById.html";
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<ApplicationUser> users = applicationUserRepo.findAll();
        model.addAttribute("users", users);
        return "users.html";
    }

    @PostMapping("/follow/{userId}")
    public RedirectView followUser(@PathVariable Long userId, Principal principal) {
        ApplicationUser loggedInUser = applicationUserRepo.findByUsername(principal.getName());

        ApplicationUser userToFollow = applicationUserRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (loggedInUser.getFollowing().contains(userToFollow)) {
            loggedInUser.getFollowing().remove(userToFollow);
        } else {
            loggedInUser.getFollowing().add(userToFollow);
        }

        applicationUserRepo.save(loggedInUser);

        return new RedirectView("/users/" + userId);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException
    {
        ResourceNotFoundException(String message)
        {
            super(message);
        }
    }
}
