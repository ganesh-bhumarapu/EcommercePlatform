//package com.EMS.ecommerce.Controller;
//
//
//import com.EMS.ecommerce.Service.AuthService;
//import com.EMS.ecommerce.dto.LoginRequest;
//import com.EMS.ecommerce.dto.AuthResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    public String register(@Valid @RequestBody RegisterRequest request){
//        return authService.register(request);
//    }
//
//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody LoginRequest request){
//        return authService.login(request);
//    }
//}

package com.EMS.ecommerce.Controller;

import com.EMS.ecommerce.Service.AuthService;
import com.EMS.ecommerce.dto.AuthResponse;
import com.EMS.ecommerce.dto.CreateUserRequest;
import com.EMS.ecommerce.dto.LoginRequest;
import com.EMS.ecommerce.dto.RegisterRequest;
import com.EMS.ecommerce.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResp = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResp = authService.login(request);
        return ResponseEntity.ok(authResp);
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResp = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResp);
    }
}