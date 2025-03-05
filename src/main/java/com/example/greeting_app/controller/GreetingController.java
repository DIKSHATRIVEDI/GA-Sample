package com.example.greeting_app.controller;

import com.example.greeting_app.model.Greeting;
import com.example.greeting_app.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.greeting_app.service.GreetingService;

import java.util.List;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService){
        this.greetingService=greetingService;
    }

    @PostMapping
    public Greeting saveGreeting(@RequestBody String message) {
        return greetingService.saveGreeting(message);
    }

    // New GET method to fetch a greeting by ID
    @GetMapping("/{id}")
    public Greeting getGreetingById(@PathVariable Long id) {
        return greetingService.getGreetingById(id);
    }

    // New GET method to fetch all greeting messages
    @GetMapping("/all")
    public List<Greeting> getAllGreetings() {
        return greetingService.getAllGreetings();
    }

    // PUT request to update a greeting by ID
    @PutMapping("/update/{id}")
    public Greeting updateGreeting(@PathVariable Long id, @RequestBody Greeting newGreeting) {
        return greetingService.updateGreeting(id, newGreeting);
    }

    // DELETE request to remove a greeting by ID
    @DeleteMapping("/delete/{id}")
    public String deleteGreeting(@PathVariable Long id) {
        boolean isDeleted = greetingService.deleteGreeting(id);
        if (isDeleted) {
            return "Greeting with ID " + id + " deleted successfully.";
        } else {
            return "Greeting with ID " + id + " not found.";
        }
    }

    @GetMapping("/personalized")
    public String getPersonalizedGreeting(@RequestParam(required = false) String firstName,
                                          @RequestParam(required = false) String lastName) {
        return "{\"message\": " + greetingService.getPersonalizedGreeting(firstName, lastName) + "\"}";
    }

}
