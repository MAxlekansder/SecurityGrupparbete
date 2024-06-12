package org.example.securitygrupparbete.Controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerSliceTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void loadHomepageWithoutLoggedinUser() throws Exception {
        mvc.perform(get("/").with(csrf())).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Admin", password = "1234", roles = "ADMIN")
    void loadHomepageWithLoggedinAdminUser() throws Exception {

        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Welcome, Admin!")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Log out")));
    }

    @Test
    @WithMockUser(username = "User", password = "1234", roles = "USER")
    void testRegisterUserPageWithUnauthorizedRole() throws Exception {
        mvc.perform(get("/register").with(csrf())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", password = "1234", roles = "ADMIN")
    void testRegisterUserPageWithAuthorizedRole() throws Exception {
        mvc.perform(get("/register").with(csrf())).andExpect(status().isOk());
    }
}
