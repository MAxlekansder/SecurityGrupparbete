package org.example.securitygrupparbete.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    public String updateUser(Model model) {         // Fredrik
        return "user";
    }

    public String registerUser(Model model) {       // Oskar
        return "user";
    }

    public String deleteUser(Model model) {         // Alexander
        return "user";
    }

    public String logoutUser(Model model) {         // Alexander
        return "user";
    }

    public String presentHomepageForUser(Model model) {     // Oskar
        return "user";
    }

    @GetMapping("/")
    public String homepage(Model model) { // Fredrik
        LOG.debug("funkar?");
        return "helloworld";
    }
}
