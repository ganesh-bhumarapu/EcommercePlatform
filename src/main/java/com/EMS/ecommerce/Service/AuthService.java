package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Entity.Role;
import com.EMS.ecommerce.Entity.User;
import com.EMS.ecommerce.Repository.UserRepository;
import com.EMS.ecommerce.Security.JwtService;
import com.EMS.ecommerce.dto.AuthResponse;
import com.EMS.ecommerce.dto.LoginRequest;
import com.EMS.ecommerce.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        return"User registered successfully";
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean passwordMathes = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if(!passwordMathes){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
