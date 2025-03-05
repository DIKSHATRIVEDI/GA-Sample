package com.example.greeting_app.repository;

import com.example.greeting_app.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {
}

