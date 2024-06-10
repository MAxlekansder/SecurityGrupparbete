package org.example.securitygrupparbete.Configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {


    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(autRequest ->
                        autRequest
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                )
                .build();
    }
}
