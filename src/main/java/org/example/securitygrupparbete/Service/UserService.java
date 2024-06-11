package org.example.securitygrupparbete.Service;


import jakarta.annotation.PostConstruct;
import org.example.securitygrupparbete.Model.UserDTO;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct // Alexander
    private void saveAdminUser(){ // Oskar
        UserDTO admin = new UserDTO();

        admin.setUsername("Admin")
            .setPassword(passwordEncoder.encode("1234"))
            .setRole("ADMIN");

        UserDTO user = new UserDTO();

        user.setUsername("User")
            .setEmail("user@mail.com")
            .setPassword(passwordEncoder.encode("1234"))
            .setRole("USER");

        userRepository.save(admin);
        userRepository.save(user);

    }


    public boolean deleteUserByEmail(String email) {
        Optional<UserDTO> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            LOG.info("Deleting user", user.get().getId(),  " ", user.get().getUsername());
            userRepository.deleteById(user.get().getId());

            return true;
        }
        return false;
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
