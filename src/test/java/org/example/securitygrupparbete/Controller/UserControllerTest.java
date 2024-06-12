package org.example.securitygrupparbete.Controller;

import org.example.securitygrupparbete.Configuration.SecurityConfiguration;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.example.securitygrupparbete.Service.UserDetailsServiceImpl;
import org.example.securitygrupparbete.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class})
@Import({SecurityConfiguration.class})
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {

    }

    @Test
    void register() {
    }

    @Test
    void homePage() {
    }

    @Test //Oskar
    @WithMockUser(username = "Admin", password ="1234")
    void loginUserSuccessfully() throws Exception {

    }

    @Test //Oskar

    void loginUserUnsuccessfully() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());


    }
}