package com.buildsphere.reporting.config;

import com.buildsphere.reporting.model.Role;
import com.buildsphere.reporting.model.User;
import com.buildsphere.reporting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .fullName("Admin User")
                        .email("admin@buildsphere.local")
                        .passwordHash(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .build());
                userRepository.save(User.builder()
                        .fullName("Coordinator User")
                        .email("coordinator@buildsphere.local")
                        .passwordHash(passwordEncoder.encode("Coordinator@123"))
                        .role(Role.COORDINATOR)
                        .build());
                userRepository.save(User.builder()
                        .fullName("Faculty User")
                        .email("faculty@buildsphere.local")
                        .passwordHash(passwordEncoder.encode("Faculty@123"))
                        .role(Role.FACULTY)
                        .build());
            }
        };
    }
}
