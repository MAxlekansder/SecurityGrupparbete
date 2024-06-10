package org.example.securitygrupparbete.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


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

    public String homepage(Model model) {       // Fredrik
        return "user";
    }
}
