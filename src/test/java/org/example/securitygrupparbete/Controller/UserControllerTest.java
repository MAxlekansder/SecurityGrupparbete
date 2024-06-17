package org.example.securitygrupparbete.Controller;

import org.example.securitygrupparbete.Configuration.SecurityConfiguration;
import org.example.securitygrupparbete.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Scanner;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest({UserService.class})
//@Import({SecurityConfiguration.class})
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test @DisplayName("Test deleting as attack with csrf enabled")
    public void testDeleteUserWithoutCSRFPostProtection() throws Exception {        // Alexander
        mvc.perform(post("/deleteUserResult")
                        .param("email", "user@mail.com")
                )
                .andExpect(status().isForbidden());

    }

    @Test @DisplayName("Test deleting as user with csrf token")
    public void testDeleteUserWithCSRFBadAuthority() throws Exception {         // Alexader
        mvc.perform(post("/deleteUserResult")
                        .param("email", "user@gmail.com").with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test @DisplayName("Test deleting as admin with csrf token and good auth")
    public void testDeleteUserWithCSRFAsAdmin() throws Exception {          // Alexander
        mvc.perform(post("/deleteUserResult")
                        .param("email", "user@gmail.com")
                        .with(user("user").roles("ADMIN"))
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test @DisplayName("Test deleting user with mockUser as admin and csrf token")
    @WithMockUser(username = "Admin", password = "1234", roles = "ADMIN")
    public void testDeleteUserWithCSRFAsAdminMock() throws Exception {          // Alexander
        mvc.perform(post("/deleteUserResult").param("email", "user@gmail.com").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("Test deleting user with mockUser as user and csrf token")
    @WithMockUser(username = "User", password = "1234", roles = "USER")
    public void testDeleteUserWithCSRFAsUserMock() throws Exception {           // Alexander
        mvc.perform(post("/deleteUserResult").param("email", "user@gmail.com").with(csrf()))
                .andExpect(status().isForbidden());
    }



    @Test @DisplayName("Test reaching registration with mockUser as user and csrf token")
    @WithMockUser(username = "User", password = "1234", roles = "USER")
    public void testIfUserCanReachRegistrationPage() throws Exception {         // Alexander
        mvc.perform(get("/register").param("username", "alexander").with(csrf()))
                .andExpect(status().isForbidden());

    }



    @Test @DisplayName("Test reaching registration with mockUser as admin and csrf token")
    @WithMockUser(username = "Admin", password = "1234", roles = "ADMIN")
    public void testIfAdminCanReachRegistrationPage() throws Exception {        // Alexander
        mvc.perform(get("/register").with(csrf())).andExpect(status().isOk());

    }
 }