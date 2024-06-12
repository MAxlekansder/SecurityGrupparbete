package org.example.securitygrupparbete.Controller;


import org.apache.catalina.security.SecurityConfig;
import org.example.securitygrupparbete.Repository.UserRepository;
import org.example.securitygrupparbete.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.ElementType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest({UserController.class})
@Import(SecurityConfig.class)
public class UserControllerSliceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;




    @Test
    void loadHomepageWithoutLoggedinUser() throws Exception {

        mvc.perform(get("/")).andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "Admin", password = "1234")
    void loadHomepageWithLoggedinAdminUser() throws Exception {

        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Welcome, Admin!")));

        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Log out")));

    }

}
