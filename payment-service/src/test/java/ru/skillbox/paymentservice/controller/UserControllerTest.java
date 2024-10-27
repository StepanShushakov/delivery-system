package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.repository.UserRepository;
import ru.skillbox.paymentservice.service.UserService;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private User user;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Configuration
    @ComponentScan(basePackageClasses = {UserController.class})
    public static class TestConf {
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(8L);
        user.setBalance(100);
    }

    @Test
    public void depositBalance() throws Exception {

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mvc.perform(post("/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "  \"userId\": 8,\n" +
                                        "  \"sum\": 22.55\n" +
                                        "}"
                                )
                )
                .andExpect(status().isOk());
    }
}
