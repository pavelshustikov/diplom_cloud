package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.AuthRequest;
import com.example.cloudstorage.entity.AppUser;
import com.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRepository.save(new AppUser(null, "test", "1234"));
    }

    @Test
    void login_returnsToken_ifCredentialsCorrect() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setLogin("test");
        request.setPassword("1234");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists());
    }

    @Test
    void login_returnsError_ifWrongPassword() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setLogin("test");
        request.setPassword("wrong");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
