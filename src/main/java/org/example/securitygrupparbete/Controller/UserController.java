package org.example.securitygrupparbete.Controller;


import org.example.securitygrupparbete.Model.UserDTO;
import org.example.securitygrupparbete.Repository.UserRepository;
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

@Controller
public class UserController {

    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String updateUser(Model model) {         // Fredrik
        return "user";
    }


    @GetMapping("/register")//Oskar
    public String register(Model model){

        model.addAttribute("user", new UserDTO());
        return "register";

    }

    @PostMapping("/register")//Oskar
    public String registerUser(@Validated @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            user.setRole("USER");
            user.setUsername(HtmlUtils.htmlEscape(user.getUsername()));
            user.setEmail(HtmlUtils.htmlEscape(user.getEmail()));
            user.setPassword(HtmlUtils.htmlEscape(passwordEncoder.encode(user.getPassword())));
            userRepository.save(user);
            LOG.info("Saving new user object" +  user.toString());

            return "saveUserSuccessfull";
        }
    }



    public String deleteUser(Model model) {         // Alexander
        return "user";
    }

    public String logoutUser(Model model) {         // Alexander
        return "user";
    }

    @GetMapping("/")
    public String presentHomepageForUser(Model model) {     // Oskar
        return "user";
    }

    @GetMapping("/homepage")
    public String homepage(Model model) { // Fredrik
        LOG.debug("funkar?");
        return "helloworld";
    }
}
