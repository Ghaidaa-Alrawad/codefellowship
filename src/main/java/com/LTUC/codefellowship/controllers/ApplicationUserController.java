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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
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
        }
        return "index.html";
    }

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
        }

        ApplicationUser applicationUser = applicationUserRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id "+id));


        m.addAttribute("username", applicationUser.getUsername());
        m.addAttribute("firstName", applicationUser.getFirstName());
        m.addAttribute("lastName", applicationUser.getLastName());
        m.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
        m.addAttribute("bio", applicationUser.getBio());
        m.addAttribute("defaultProfilePicture", "https://i.pinimg.com/564x/b6/80/b9/b680b917d8b5e428d6dadca3a15684bb.jpg");

        return "usersById.html";
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
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
