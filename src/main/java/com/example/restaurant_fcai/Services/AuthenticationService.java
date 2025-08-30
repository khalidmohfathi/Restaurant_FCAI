package com.example.restaurant_fcai.Services;

import com.example.restaurant_fcai.DTO.LoginRequest;
import com.example.restaurant_fcai.DTO.LoginResponse;
import com.example.restaurant_fcai.DTO.PasswordResetConfirmRequest;
import com.example.restaurant_fcai.DTO.PasswordResetRequest;
import com.example.restaurant_fcai.DTO.SignupRequest;
import com.example.restaurant_fcai.DTO.UserResponse;
import com.example.restaurant_fcai.Entities.Customer;
import com.example.restaurant_fcai.Entities.User;
import com.example.restaurant_fcai.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserResponse signup(SignupRequest request) {

   
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = new Customer(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword()
                );

        User savedUser = userService.createUser(user);
        
        return userService.toUserResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
     
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return LoginResponse.builder()
                .token(token)
                .user(userService.toUserResponse(user))
                .build();
    }

    public void requestPasswordReset(PasswordResetRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(1); // Token expires in 1 hour

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(expiryTime);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }
    public void confirmPasswordReset(PasswordResetConfirmRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

} 