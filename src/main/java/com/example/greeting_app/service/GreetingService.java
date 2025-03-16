package com.example.greeting_app.service;

import com.example.greeting_app.model.Greeting;
import com.example.greeting_app.repository.GreetingRepository;
import com.example.greeting_app.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Optional;

@Service
public class GreetingService {

    @Autowired
    private GreetingRepository greetingRepository;

    @Autowired
    private JwtToken tokenUtil;

    @CacheEvict(value = "greetingCache", allEntries = true) // Clear cache on new entry
    public Greeting saveGreeting(String message, Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            Greeting greeting = new Greeting(message);
            return greetingRepository.save(greeting);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    @Cacheable(value = "greetingCache", key = "#id") // Cache specific entry by ID
    public Greeting getGreetingById(Long id, Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            return greetingRepository.findById(id).orElse(null);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    @Cacheable(value = "greetingCache") // Cache results
    public List<Greeting> getAllGreetings(Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            return greetingRepository.findAll();
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    @Caching(evict = {
            @CacheEvict(value = "greetingCache", allEntries = true),
            @CacheEvict(value = "greetingCache", key = "#id")
    })
    public Greeting updateGreeting(Long id, Greeting newGreeting, Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            Optional<Greeting> existingGreeting = greetingRepository.findById(id);
            if (existingGreeting.isPresent()) {
                Greeting greeting = existingGreeting.get();
                greeting.setMessage(newGreeting.getMessage());
                return greetingRepository.save(greeting);
            }
            return null;
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    @CacheEvict(value = "greetingCache", key = "#id") // Remove from cache on delete
    public boolean deleteGreeting(Long id, Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            if (greetingRepository.existsById(id)) {
                greetingRepository.deleteById(id);
                return true;
            }
            return false;
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    public String getPersonalizedGreeting(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return "Hello, " + firstName + " " + lastName + "!";
        } else if (firstName != null) {
            return "Hello, " + firstName + "!";
        } else if (lastName != null) {
            return "Hello, " + lastName + "!";
        } else {
            return "Hello World!";
        }
    }
}
