package com.example.greeting_app.service;

import com.example.greeting_app.Interface.IAuthenticationService;
import com.example.greeting_app.dto.AuthUserDTO;
import com.example.greeting_app.dto.ForgotPasswordDTO;
import com.example.greeting_app.dto.LoginDTO;
import com.example.greeting_app.exception.UserException;
import com.example.greeting_app.model.AuthUser;
import com.example.greeting_app.repository.AuthUserRepository;
import com.example.greeting_app.util.EmailSenderService;
import com.example.greeting_app.util.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    JwtToken tokenUtil;

    @Autowired
    EmailSenderService emailSenderService;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        try {
            AuthUser user = new AuthUser(userDTO);
            System.out.println(user);
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);

            String token = tokenUtil.createToken(user.getUserId());
            authUserRepository.save(user);

            // Send email safely
            try {
                emailSenderService.sendEmail(
                        user.getEmail(),
                        "Registered in Greeting App",
                        "Hi " + user.getFirstName() + ",\n\nYou have been successfully registered!\n\nYour details:\n\n" +
                                "User Id: " + user.getUserId() + "\nFirst Name: " + user.getFirstName() + "\nLast Name: " + user.getLastName() +
                                "\nEmail: " + user.getEmail() + "\nToken: " + token
                );
            } catch (Exception emailException) {
                System.err.println("Error sending email: " + emailException.getMessage());
            }

            return user;
        } catch (Exception e) {
            throw new UserException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public String login(LoginDTO loginDTO){
        Optional<AuthUser> user= Optional.ofNullable(authUserRepository.findByEmail(loginDTO.getEmail()));
        if (user.isPresent()){
            if (passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            emailSenderService.sendEmail(user.get().getEmail(), "Logged in Successfully!", "Hi "
                    + user.get().getFirstName() + ",\n\nYou have successfully logged in into Greeting App!");

            return "Congratulations!! You have logged in successfully!";
        } else {
            throw new UserException("Sorry! Email or Password is incorrect!");
        }
    } else {
        throw new UserException("Sorry! Email or Password is incorrect!");
    }
}

    public String forgotPassword(String email, ForgotPasswordDTO forgotPasswordDTO) {
        Optional<AuthUser> userOptional = Optional.ofNullable(authUserRepository.findByEmail(email));

        if (!userOptional.isPresent()) {
            throw new UserException("Sorry! We cannot find the user email: " + email);
        }

        AuthUser user = userOptional.get();
        String newPassword = forgotPasswordDTO.getPassword();

        user.setPassword(passwordEncoder.encode(newPassword)); // Hash new password
        authUserRepository.save(user); // Update password in DB

        // Send confirmation email
        emailSenderService.sendEmail(
                user.getEmail(),
                "Password Changed Successfully!",
                "Hi " + user.getFirstName() + ",\n\nYour password has been successfully updated."
        );

        return "Password has been changed successfully!";
    }

    @Override
    public String resetPassword(String email, String currentPassword, String newPassword) {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect!");
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        authUserRepository.save(user);

        emailSenderService.sendEmail(user.getEmail(),
                "Password Reset Successful",
                "Hi " + user.getFirstName() + ",\n\nYour password has been successfully updated!");

        return "Password reset successfully!";
    }


}


