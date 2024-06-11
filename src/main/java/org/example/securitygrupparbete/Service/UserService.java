package org.example.securitygrupparbete.Service;


import jakarta.annotation.PostConstruct;
import org.example.securitygrupparbete.Model.UserAuthentication;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    private void saveAdminUser(){
        UserAuthentication user = new UserAuthentication();

        user.setUsername("Admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("ADMIN");

        userRepository.save(user);
    }


}
