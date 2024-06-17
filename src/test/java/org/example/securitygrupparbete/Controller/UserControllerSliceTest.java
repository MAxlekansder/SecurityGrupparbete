package org.example.securitygrupparbete.Controller;


import org.example.securitygrupparbete.Configuration.SecurityConfiguration;
import org.example.securitygrupparbete.Service.UserDetailsServiceImpl;
import org.example.securitygrupparbete.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerSliceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userServiceImpl;
    @MockBean
    private UserService userService;

    @Test
    void loadHomepageWithoutLoggedinUser() throws Exception {
        mvc.perform(get("/").with(csrf())).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "Admin")
    void loadHomepageWithLoggedinAdminUser() throws Exception {

        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Welcome, Admin!")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Log out")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetRegisterUserPageWithUnauthorizedRole() throws Exception {
        mvc.perform(get("/register").with(csrf())).andExpect(status().isForbidden());
    }

   @Test
   @WithMockUser(roles = "ADMIN")
   void testPostRegisterUserPageWithUnauthorizedRole() throws Exception {

       String userForm = "firstName=Oskar" +
                         "&" +
                         "email=oskar";

       mvc.perform(post("/register")
               .with(csrf())
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                       .content(userForm))
               .andExpect(status()
                       .isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeHasFieldErrors("user","email"))
               .andExpect(content().string(org.hamcrest.Matchers.containsString("Email should be valid"))
               )
               .andExpect(model().attributeHasFieldErrorCode("user","password","NotBlank"))
       ;
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void testRegisterUserSuccessfully() throws Exception {
       String userForm = "firstName=Oskar" +
                         "&" +
                         "lastName=Johansson" +
                         "&" +
                         "age=37" +
                         "&" +
                         "username=oskar" +
                         "&" +
                         "email=oskar@mail.com" +
                         "&" +
                         "password=oskar";

        mvc.perform(post("/register").with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .content(userForm))
               .andExpect(status().isOk())
               .andExpect(content().string(org.hamcrest.Matchers.containsString("Your user is now saved")));
   }


    @Test
    void testRegisterUserPageWithAuthorizedRole() throws Exception {
        mvc.perform(get("/register").with(csrf()).with(user("user").roles("ADMIN"))).andExpect(status().isOk());
    }
}
