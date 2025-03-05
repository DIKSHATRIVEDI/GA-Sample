package com.example.greeting_app.service;

import com.example.greeting_app.model.Greeting;
import com.example.greeting_app.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GreetingService {

    @Autowired
    private GreetingRepository greetingRepository;

    public Greeting saveGreeting(String message) {
        Greeting greeting = new Greeting(message);
        return greetingRepository.save(greeting);
    }

    // New method to find a greeting by ID
    public Greeting getGreetingById(Long id) {
        Optional<Greeting> greeting = greetingRepository.findById(id);
        return greeting.orElse(null); // Returns null if not found
    }

    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }

    // Method to update an existing greeting message
    public Greeting updateGreeting(Long id, Greeting newGreeting) {
        Optional<Greeting> existingGreeting = greetingRepository.findById(id);
        if (existingGreeting.isPresent()) {
            Greeting greeting = existingGreeting.get();
            greeting.setMessage(newGreeting.getMessage());  // Update message
            return greetingRepository.save(greeting);  // Save updated greeting
        }
        return null;  // Return null if ID not found
    }

    // Method to delete a greeting message by ID
    public boolean deleteGreeting(Long id) {
        if (greetingRepository.existsById(id)) {
            greetingRepository.deleteById(id);
            return true;  // Return true if deletion was successful
        }
        return false;  // Return false if ID was not found
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
