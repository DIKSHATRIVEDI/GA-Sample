package com.example.greeting_app.service;

import com.example.greeting_app.Interface.IAuthenticationService;
import com.example.greeting_app.dto.AuthUserDTO;
import com.example.greeting_app.dto.LoginDTO;
import com.example.greeting_app.exception.UserException;
import com.example.greeting_app.model.AuthUser;
import com.example.greeting_app.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthUserRepository authUserRepository;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        AuthUser user = new AuthUser(userDTO);
        authUserRepository.save(user);

        return user;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(loginDTO.getEmail()));

        if (user.isPresent()) {


                return "Congratulations!! You have logged in successfully!";
            } else {
                throw new UserException("Sorry! Email or Password is incorrect!");
            }
        }
    }
