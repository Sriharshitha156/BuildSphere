package com.buildsphere.reporting.service;

import com.buildsphere.reporting.dto.AuthRequest;
import com.buildsphere.reporting.dto.AuthResponse;
import com.buildsphere.reporting.dto.RegisterRequest;
import com.buildsphere.reporting.model.User;
import com.buildsphere.reporting.repository.UserRepository;
import com.buildsphere.reporting.security.JwtService;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = userRepository.save(User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build());

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build(),
                Map.of("role", user.getRole().name(), "uid", user.getId())
        );
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build(),
                Map.of("role", user.getRole().name(), "uid", user.getId())
        );

        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
