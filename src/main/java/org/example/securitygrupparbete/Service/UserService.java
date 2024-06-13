package org.example.securitygrupparbete.Service;


import jakarta.annotation.PostConstruct;
import org.example.securitygrupparbete.DTO.UserDTO;
import org.example.securitygrupparbete.Model.UserModel;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

import static org.example.securitygrupparbete.Service.MaskingService.maskEmail;

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
        UserModel admin = new UserModel();

        admin.setUsername("Admin")
            .setPassword(passwordEncoder.encode("1234"))
            .setRole("ADMIN");

        UserModel user = new UserModel();

        user.setUsername("User")
            .setEmail("user@mail.com")
            .setPassword(passwordEncoder.encode("1234"))
            .setRole("USER");

        userRepository.save(admin);
        userRepository.save(user);

    }


    public boolean deleteUserByEmail(String email) {
        Optional<UserModel> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            LOG.info("deleting userid {} with username {}", user.get().getId(), user.get().getUsername());
            userRepository.deleteById(user.get().getId());

            return true;
        }
        throw new UsernameNotFoundException("User with email " + MaskingService.maskEmail(email) + " was not found");
    }

   
    
    public boolean updatePassword(String email, String password) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            UserModel user = userOptional.get();
            user.setPassword(HtmlUtils.htmlEscape(passwordEncoder.encode(password)));
            userRepository.save(user);
            return true;
        }
        throw new UsernameNotFoundException("User with email " + MaskingService.maskEmail(email) + " was not found");

    }

    public boolean registerUser(UserDTO user) {

        UserModel userToSave = new UserModel();

        userToSave.setRole("USER")
                    .setUsername(HtmlUtils.htmlEscape(user.getUsername()))
                    .setEmail(HtmlUtils.htmlEscape(user.getEmail()))
                    .setPassword(HtmlUtils.htmlEscape(passwordEncoder.encode(user.getPassword())));
            userRepository.save(userToSave);


            LOG.info("Saving new user object. Username: {}, Masking email: {} ", userToSave.getUsername(), maskEmail(userToSave.getEmail()));
            return true;


    }
    
    

}
