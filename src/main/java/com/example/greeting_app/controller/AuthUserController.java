package com.example.greeting_app.controller;

import com.example.greeting_app.dto.AuthUserDTO;
import com.example.greeting_app.dto.ForgotPasswordDTO;
import com.example.greeting_app.dto.LoginDTO;
import com.example.greeting_app.dto.ResponseDTO;
import com.example.greeting_app.model.AuthUser;
import com.example.greeting_app.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthUserController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody AuthUserDTO userDTO) throws Exception{
        AuthUser user=authenticationService.register(userDTO);
        ResponseDTO responseUserDTO =new ResponseDTO("User details is submitted!",user);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        String result=authenticationService.login(loginDTO);
        ResponseDTO responseUserDTO=new ResponseDTO("Login successfully!!",result);
        return  new ResponseEntity<>(responseUserDTO,HttpStatus.OK);
    }

    @PutMapping("/forgotPassword/{email}")
    public ResponseEntity<ResponseDTO> forgotPassword(@PathVariable String email,
                                                      @Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        String response = authenticationService.forgotPassword(email, forgotPasswordDTO);
        return new ResponseEntity<>(new ResponseDTO(response, null), HttpStatus.OK);
    }

    @PutMapping("/resetPassword/{email}")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String email,
                                                     @RequestParam String currentPassword,
                                                     @RequestParam String newPassword) {
        String response = authenticationService.resetPassword(email, currentPassword, newPassword);
        return new ResponseEntity<>(new ResponseDTO(response, null), HttpStatus.OK);
    }
}