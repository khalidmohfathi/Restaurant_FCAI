package com.example.restaurant_fcai.DTO;

import com.example.restaurant_fcai.Services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/login")
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

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        // Note: JWT tokens are stateless, so logout is handled client-side
        // by removing the token from storage
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Logged out successfully")
                .build());
    }

    @PostMapping("/reset-password")
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

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<MessageResponse> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        try {
            authenticationService.confirmPasswordReset(request);
            return ResponseEntity.ok(MessageResponse.builder()
                    .message("Password reset successfully")
                    .build());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Invalid or expired")) {
                return ResponseEntity.badRequest().body(MessageResponse.builder()
                        .message("Invalid or expired reset token")
                        .build());
            }
            return ResponseEntity.badRequest().body(MessageResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/debug/user/{email}")
    public ResponseEntity<?> debugUser(@PathVariable String email) {
        try {
            // This is a debug endpoint to check if user exists
            return ResponseEntity.ok(MessageResponse.builder()
                    .message("Debug endpoint - check application logs for user existence")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.builder()
                    .message("Error: " + e.getMessage())
                    .build());
        }
    }
} 