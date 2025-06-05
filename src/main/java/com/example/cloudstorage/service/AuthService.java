package com.example.cloudstorage.service;

import com.example.cloudstorage.entity.AppUser;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.security.AuthTokenStore;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// проверяет пароль и возвращает токен
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenStore tokenStore;

    public String login(String login, String password) {
        AppUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return tokenStore.generateToken(user.getLogin());
    }
}