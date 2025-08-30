package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.DTO.LoginRequest;
import com.example.restaurant_fcai.DTO.LoginResponse;
import com.example.restaurant_fcai.DTO.MessageResponse;
import com.example.restaurant_fcai.DTO.PasswordResetConfirmRequest;
import com.example.restaurant_fcai.DTO.PasswordResetRequest;
import com.example.restaurant_fcai.DTO.SignupRequest;
import com.example.restaurant_fcai.DTO.UserResponse;
import com.example.restaurant_fcai.Services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            UserResponse user = authenticationService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.badRequest().body(MessageResponse.builder()
                        .message("User with this email already exists")
                        .build());
            }
            return ResponseEntity.badRequest().body(MessageResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageResponse.builder()
                    .message("Invalid email or password")
                    .build());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder()
                    .message("User not found")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(MessageResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logout() {
        // Note: JWT tokens are stateless, so logout is handled client-side
        // by removing the token from storage
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Logged out successfully")
                .build());
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<MessageResponse> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        try {
            authenticationService.requestPasswordReset(request);
            return ResponseEntity.ok(MessageResponse.builder()
                    .message("Reset link sent")
                    .build());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder()
                        .message("User not found with this email")
                        .build());
            }
            return ResponseEntity.badRequest().body(MessageResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    // @PostMapping("/resetpassword/confirm")
    // public ResponseEntity<MessageResponse> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
    //     try {
    //         authenticationService.confirmPasswordReset(request);
    //         return ResponseEntity.ok(MessageResponse.builder()
    //                 .message("Password reset successfully")
    //                 .build());
    //     } catch (RuntimeException e) {
    //         if (e.getMessage().contains("Invalid or expired")) {
    //             return ResponseEntity.badRequest().body(MessageResponse.builder()
    //                     .message("Invalid or expired reset token")
    //                     .build());
    //         }
    //         return ResponseEntity.badRequest().body(MessageResponse.builder()
    //                 .message(e.getMessage())
    //                 .build());
    //     }
    // }

} 