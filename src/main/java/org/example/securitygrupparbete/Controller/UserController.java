package org.example.securitygrupparbete.Controller;


import org.example.securitygrupparbete.DTO.UserDTO;
import org.example.securitygrupparbete.Model.UserModel;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.example.securitygrupparbete.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.Arrays;

import static org.example.securitygrupparbete.Service.MaskingService.maskEmail;

@Controller
public class UserController {
    
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    
    
    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    
    
    @GetMapping("/update")
    public String getUpdate(Model model) {
        model.addAttribute("user", new UserDTO());
        return "update";
    }
    
    
    @PostMapping("/update")
    public String updateUser(@Validated @ModelAttribute("user") UserDTO user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "You must fill in all the fields");
            LOG.warn("Fields in update-form mismatch the userDTO " + result.getAllErrors());
            return "error/updateUserFail";
            
        }
        
        boolean success = false;
        
        try {
            success = userService.updatePassword(user.getEmail(), user.getPassword());
        } catch (UsernameNotFoundException e) {
            LOG.warn("User with email: " + maskEmail(user.getEmail()) + " could not be found");
            LOG.warn(Arrays.toString(e.getStackTrace()));
        }
        
        if (success) {
            model.addAttribute("userEmail", maskEmail(user.getEmail()));
            LOG.info("User with email: " + maskEmail(user.getEmail()) + " has been updated");
            return "updateUserSuccessful";
        } else {
            model.addAttribute("error", "User could not be found");
            return "error/updateUserFail";
        }
        
    }
    
    
    @GetMapping("/register")//Oskar
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
        
    }
    
    
    @PostMapping("/register")//Oskar
    public String registerUser(@Validated @ModelAttribute("user") UserDTO user, BindingResult
            bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            LOG.warn("Binding result error in post register {},", bindingResult.hasErrors());
            
            return "register";
            
        } else {
            userService.registerUser(user);
            return "saveUserSuccessful";
        }
        
    }
    
    
    @GetMapping("/deleteUser")
    public String deleteUserForm() {
        return "deleteUser";
    }
    
    
    @PostMapping("/deleteUserResult")
    public String deleteUser(@RequestParam String email, Model model) {         // Alexander
        LOG.info("delete user with email " + maskEmail(email));
        //  model.addAttribute("message", userService.deleteUserByEmail(email) ? "user deleted successfully" : "failed to delete user");
        //  return "deletedUser";
        
        boolean deletedUser = false;
        
        try {
            deletedUser = userService.deleteUserByEmail(email);
            
        } catch (UsernameNotFoundException e) {
            LOG.warn("User could not be found");
            LOG.warn(Arrays.toString(e.getStackTrace()));
            LOG.info(String.valueOf(deletedUser));
        }
        
        if (deletedUser) {
            LOG.info("user deleted successfully {}", maskEmail(email));
            model.addAttribute("message", "user deleted successful");
        } else {
            LOG.error("failed to delete user");
            model.addAttribute("message", "failed to delete user");
        }
        return "deleteUserResult";
        
    }
    
    
    @GetMapping("/logoutSuccess")                                         // Alexander
    public String logoutUser(Model model) {            // redan clearat allt här, principal redan borta
        model.addAttribute("message", "you've been logged out, redirecting to log in...");
        return "logoutSuccess";
        
    }
    
    
    @GetMapping("/")
    public String homePage(Principal principal, Model model) {     // Oskar
        
        model.addAttribute("user", principal);
        LOG.info("Logging principal in root: {}", principal.getName());
        
        return "homepage";
    }
    
    
    @GetMapping("/admin")
    public String adminPage(Model model) {
        return "adminpage";
    }
    
    
}

