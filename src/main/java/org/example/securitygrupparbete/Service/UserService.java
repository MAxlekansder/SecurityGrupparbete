package org.example.securitygrupparbete.Service;


import jakarta.annotation.PostConstruct;
import org.example.securitygrupparbete.Model.UserDTO;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    @PostConstruct
    private void saveAdminUser(){
        UserDTO user = new UserDTO();

        user.setUsername("Admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("ADMIN");

        userRepository.save(user);

    }


    public boolean deleteUserByEmail(String email) {
        Optional<UserDTO> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            userRepository.deleteById(user.get().getId());
            return true;
        }
        throw new UsernameNotFoundException("No user found");
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
