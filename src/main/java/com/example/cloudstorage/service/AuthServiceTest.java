package com.example.cloudstorage.service;


import com.example.cloudstorage.entity.AppUser;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.security.AuthTokenStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthServiceTest(UserRepository userRepository, AuthTokenStore tokenStore) {
        this.userRepository = userRepository;
        this.authService = new AuthService(userRepository, tokenStore);
    }

    @Test
    void login_shouldReturnToken_whenUserExistsAndPasswordCorrect() {
        AppUser user = new AppUser(null, "testuser", "testpass");
        userRepository.save(user);

        String token = authService.login("testuser", "testpass");
        assertNotNull(token);
    }

    @Test
    void login_shouldThrowException_whenPasswordInvalid() {
        AppUser user = new AppUser(null, "baduser", "pass");
        userRepository.save(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login("baduser", "wrong");
        });

        assertEquals("Invalid password", ex.getMessage());
    }
}