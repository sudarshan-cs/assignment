package com.example.assignment.controllers;


import com.example.assignment.dto.AuthRequest;
import com.example.assignment.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        if ("admin".equalsIgnoreCase(authRequest.getUsername())) {
            String token = jwtTokenUtil.generateToken(authRequest.getUsername(), "ADMIN");
            return ResponseEntity.ok(token);
        } else if ("user".equalsIgnoreCase(authRequest.getUsername())) {
            String token = jwtTokenUtil.generateToken(authRequest.getUsername(), "USER");
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
