package com.example.greeting_app.controller;

import com.example.greeting_app.util.JwtToken;
import com.example.greeting_app.model.Greeting;
import com.example.greeting_app.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.greeting_app.service.GreetingService;

import java.util.List;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @Autowired
    GreetingService greetingService;
    @Autowired
    JwtToken jwtToken;

    @Autowired
    public GreetingController(GreetingService greetingService, JwtToken jwtToken) {
        this.greetingService = greetingService;
        this.jwtToken = jwtToken;
    }

    // Save a new greeting (User must be logged in)
    @PostMapping("/create")
    public Greeting saveGreeting(@RequestParam Long userId, @RequestParam String token, @RequestBody String message) {
        if (jwtToken.isUserLoggedIn(userId, token)) {
            return greetingService.saveGreeting(message, userId, token);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    // Get greeting by ID (User must be logged in)
    @GetMapping("/{id}")
    public Greeting getGreetingById(@PathVariable Long id, @RequestParam Long userId, @RequestParam String token) {
        if (jwtToken.isUserLoggedIn(userId, token)) {
            return greetingService.getGreetingById(id, userId, token);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    // Get all greetings (User must be logged in)
    @GetMapping("/all")
    public List<Greeting> getAllGreetings(@RequestParam Long userId, @RequestParam String token) {
        if (jwtToken.isUserLoggedIn(userId, token)) {
            return greetingService.getAllGreetings(userId, token);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    // Update a greeting (User must be logged in)
    @PutMapping("/update/{id}")
    public Greeting updateGreeting(@PathVariable Long id, @RequestParam Long userId, @RequestParam String token, @RequestBody Greeting newGreeting) {
        if (jwtToken.isUserLoggedIn(userId, token)) {
            return greetingService.updateGreeting(id, newGreeting, userId, token);
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    // Delete a greeting (User must be logged in)
    @DeleteMapping("/delete/{id}")
    public String deleteGreeting(@PathVariable Long id, @RequestParam Long userId, @RequestParam String token) {
        if (jwtToken.isUserLoggedIn(userId, token)) {
            boolean isDeleted = greetingService.deleteGreeting(id, userId, token);
            return isDeleted ? "Greeting with ID " + id + " deleted successfully."
                    : "Greeting with ID " + id + " not found.";
        }
        throw new RuntimeException("Unauthorized! Please log in first.");
    }

    // Get a personalized greeting (No authentication required)
    @GetMapping("/personalized")
    public String getPersonalizedGreeting(@RequestParam(required = false) String firstName,
                                          @RequestParam(required = false) String lastName) {
        return "{\"message\": \"" + greetingService.getPersonalizedGreeting(firstName, lastName) + "\"}";
    }

}
