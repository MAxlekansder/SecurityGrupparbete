package org.example.securitygrupparbete.Service;


import org.example.securitygrupparbete.Model.UserAuthentication;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private void saveAdminUser(){
        UserAuthentication user = new UserAuthentication();

        user.setUsername("Admin");
        user.setPassword("1234");
        user.setRole("ADMIN");

        userRepository.save(user);
    }


}
