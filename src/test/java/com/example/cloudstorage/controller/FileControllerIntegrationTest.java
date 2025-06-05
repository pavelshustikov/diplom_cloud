package com.example.cloudstorage.controller;

import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class FileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @JavaDispatcher.Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    public void testListFilesIntegration() throws Exception {
        // Предварительно добавить файлы в базу данных

        mockMvc.perform(get("/files")
                        .header("auth-token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}