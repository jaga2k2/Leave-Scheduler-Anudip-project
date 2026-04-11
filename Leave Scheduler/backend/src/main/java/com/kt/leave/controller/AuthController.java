package com.kt.leave.controller;

import com.kt.leave.dto.AuthDtos.*;
import com.kt.leave.dto.CreateUserRequest;
import com.kt.leave.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/bootstrap-admin")
    public ResponseEntity<LoginResponse> bootstrapAdmin(
            @RequestHeader("X-Admin-Setup-Key") String setupKey,
            @RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(authService.bootstrapAdmin(req, setupKey));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest req) {
        authService.changePassword(userDetails.getUsername(), req);
        return ResponseEntity.ok("Password changed successfully");
    }
}
