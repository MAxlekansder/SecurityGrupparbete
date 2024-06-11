package org.example.securitygrupparbete.Controller;

import org.example.securitygrupparbete.Configuration.SecurityConfiguration;
import org.example.securitygrupparbete.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserService.class})
@Import({SecurityConfiguration.class})
class UserControllerTest {

    @Autowired
    MockMvc mvc;
}