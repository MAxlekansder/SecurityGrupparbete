package org.example.securitygrupparbete.Controller;

import org.example.securitygrupparbete.Configuration.SecurityConfiguration;
import org.example.securitygrupparbete.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Test
    public void testDeleteUserWithoutCSRFPostProtection() throws Exception {
        mvc.perform(post("/deleteUserResult")
                        .param("email", "user@mail.com")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUserWithCSRFBadAuthority() throws Exception {
        mvc.perform(post("/deleteUserResult")
                        .param("email", "user@gmail.com").with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }
}