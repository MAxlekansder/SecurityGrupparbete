package org.example.securitygrupparbete.Configuration;

/*


 */

import org.example.securitygrupparbete.Service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrfCustomizer ->                      // stoppar scripts från att sno cookien (withHttpOnlyFalse)
                        csrfCustomizer                       // nödvändig eftersom vi skickar forms från clientsidan till servern
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(autRequest ->
                        autRequest
                                .requestMatchers("/").authenticated()
                              //  .requestMatchers("/logout").authenticated() verkar som att spring overridar säkerheten oavsett
                                .requestMatchers( "/admin"
                                                    , "/update"
                                                    , "/deleteUser"
                                                    , "/deleteUserResult"
                                                    , "/register")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/logoutSuccess")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                                .permitAll()
                )
                .headers(headers ->
                        headers
                                .contentSecurityPolicy(policy ->    // stoppar XSS-attacker och scripts utifrån med 'self'
                                        policy                      // vi kan tillåta betrodda sidor, genom att inkludera dem i policyDirectives
                                                .policyDirectives("script-src 'self'")
                                )
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


}
