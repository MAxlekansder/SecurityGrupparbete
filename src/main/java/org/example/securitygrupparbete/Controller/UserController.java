package org.example.securitygrupparbete.Controller;


import org.example.securitygrupparbete.Model.UserDTO;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.example.securitygrupparbete.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;
import org.example.securitygrupparbete.Service.MaskingService;

import java.security.Principal;

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
            return "update";
        }
        boolean success = userService.updatePassword(user.getEmail(), user.getPassword());
        if (success) {
            return "updateUserSuccessfull";
        } else {
            model.addAttribute("error", "User could not be found");
            return "update";
        }
        
    }
    
    
    @GetMapping("/register")//Oskar
    public String register(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
        
    }
    
    
    @PostMapping("/register")//Oskar
    public String registerUser(@Validated @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {



        if (bindingResult.hasErrors()) {
            return "register";
        } else {

            user.setRole("USER")
                .setUsername(HtmlUtils.htmlEscape(user.getUsername()))
                .setEmail(HtmlUtils.htmlEscape(user.getEmail()))
                .setPassword(HtmlUtils.htmlEscape(passwordEncoder.encode(user.getPassword())));
                userRepository.save(user);
            LOG.info("Saving new user object " + "Username:" + user.getUsername() + "Masking email: " + MaskingService.maskEmail(user.getEmail()));

            return "saveUserSuccessful";
        }
    }




    @PostMapping("/deleteUser")
    public String deleteUser(Model model) {         // Alexander

        return "user";
    }
    
    
    @GetMapping("/logout")
    public String logoutUser(Model model) {         // Alexander

        return "logout";
    }
    
    
    @GetMapping("/")
    public String homePage(Principal principal, Model model) {     // Oskar

        if(principal == null){
            return"homepage";
        }

        model.addAttribute("user", principal);
        LOG.info("LOGGING PRINCIPALE NAME IN HOME PAGE CONTROLLER " + principal.getName());

        return "homepage";
    }
    
}
