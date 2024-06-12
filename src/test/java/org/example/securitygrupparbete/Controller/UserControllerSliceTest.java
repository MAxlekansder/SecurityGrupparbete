package org.example.securitygrupparbete.Controller;


import org.example.securitygrupparbete.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest({UserController.class})
public class UserControllerSliceTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private UserRepository userRepository;



    @Test
    void loadHomepageWithoutLoggedinUser() throws Exception {

        mvc.perform(get("/")).andExpect(status().isOk());

    }



}
