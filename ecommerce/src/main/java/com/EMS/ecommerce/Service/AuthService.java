//package com.EMS.ecommerce.Service;
//
//import com.EMS.ecommerce.Entity.Role;
//import com.EMS.ecommerce.Entity.User;
//import com.EMS.ecommerce.Repository.UserRepository;
//import com.EMS.ecommerce.Security.JwtService;
//import com.EMS.ecommerce.dto.AuthResponse;
//import com.EMS.ecommerce.dto.CreateUserRequest;
//import com.EMS.ecommerce.dto.LoginRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import com.EMS.ecommerce.dto.UserResponse; // create below or reuse existing DTO
//
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    // ── Public registration ──────────────────────────────────────────────────
//    // Role is always ROLE_USER; clients cannot influence it.
//    public String register(RegisterRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return "Email already registered";
//        }
//
//        User user = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.ROLE_USER)   // always USER from public endpoint
//                .build();
//
//        userRepository.save(user);
//        return "User registered successfully";
//    }
//
//    // ── Admin-only user creation ─────────────────────────────────────────────
//    // Role is taken from the request body; Spring Security ensures only
//    // a ROLE_ADMIN caller can reach the endpoint that calls this method.
//    public String createUser(CreateUserRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return "Email already registered";
//        }
//
//        User user = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole())   // ROLE_USER or ROLE_ADMIN as chosen by admin
//                .build();
//
//        userRepository.save(user);
//        return "User created successfully with role: " + request.getRole();
//    }
//
//    // ── Login ────────────────────────────────────────────────────────────────
//    public AuthResponse login(LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        boolean passwordMatches = passwordEncoder.matches(
//                request.getPassword(),
//                user.getPassword()
//        );
//        if (!passwordMatches) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        String token = jwtService.generateToken(user.getEmail());
//        return new AuthResponse(token, user.getName(), user.getRole().name());
//    }
//}


package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Entity.Role;
import com.EMS.ecommerce.Entity.User;
import com.EMS.ecommerce.Repository.UserRepository;
import com.EMS.ecommerce.Security.JwtService;
import com.EMS.ecommerce.dto.ApiResponse;
import com.EMS.ecommerce.dto.AuthResponse;
import com.EMS.ecommerce.dto.CreateUserRequest;
import com.EMS.ecommerce.dto.LoginRequest;
import com.EMS.ecommerce.dto.RegisterRequest;
import com.EMS.ecommerce.dto.UserResponse;
import com.EMS.ecommerce.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ── Public registration ──────────────────────────────────────────────────
    // Role is always ROLE_USER; clients cannot influence it.
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)   // always USER from public endpoint
                .build();

        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    // ── Admin-only user creation ─────────────────────────────────────────────
    // Role is taken from the request body; Spring Security ensures only
    // a ROLE_ADMIN caller can reach the endpoint that calls this method.
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())   // ROLE_USER or ROLE_ADMIN as chosen by admin
                .build();

        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    // ── Login ────────────────────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }
}
