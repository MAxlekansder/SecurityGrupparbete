package org.example.securitygrupparbete.Service;


import org.example.securitygrupparbete.Model.UserAuthentication;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository userRepository;


    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAuthentication> user = userRepository.findByUsername(username);

        if(user.isPresent()){
            var userObj = user.get();
            LOG.info("Logging in user " + userObj.getUsername());
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(userObj.getRole())
                    .build();
        }

        else {
            LOG.info("User not found for username:" + username);
            throw new UsernameNotFoundException(username);
        }
    }
}

