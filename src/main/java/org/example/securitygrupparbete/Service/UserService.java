package org.example.securitygrupparbete.Service;


import jakarta.annotation.PostConstruct;
import org.example.securitygrupparbete.Model.UserDTO;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct //Alex
    private void saveAdminUser(){ // Oskar
        UserDTO admin = new UserDTO();

        admin.setUsername("Admin");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setRole("ADMIN");

        UserDTO user = new UserDTO();

        user.setUsername("User");
        user.setEmail("user@mail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("USER");

        userRepository.save(admin);
        userRepository.save(user);

    }


    public boolean deleteUserByEmail() {
        
    }

   
    
    public boolean updatePassword(String email, String password) {
        Optional<UserDTO> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            UserDTO user = userOptional.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
        
    }
    
    

}
