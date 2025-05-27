package com.example.cloudstorage.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthTokenStore {
    private final Map<String, String> tokenStorage = new ConcurrentHashMap<>();

    public String generateToken(String login) {
        String token = UUID.randomUUID().toString();
        tokenStorage.put(token, login);
        return token;
    }

    public String getLogin(String token) {
        return tokenStorage.get(token);
    }

    public void removeToken(String token) {
        tokenStorage.remove(token);
    }
}