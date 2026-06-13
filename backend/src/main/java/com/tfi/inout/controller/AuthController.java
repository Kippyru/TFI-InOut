package com.tfi.inout.controller;

import com.tfi.inout.dto.AuthResponseDto;
import com.tfi.inout.dto.LoginRequestDto;
import com.tfi.inout.model.User;
import com.tfi.inout.security.CustomUserDetailsService;
import com.tfi.inout.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", ((User) userDetails).getRole().getId());

        final String jwt = jwtService.generateToken(extraClaims, userDetails);

        return ResponseEntity.ok(new AuthResponseDto(jwt));
    }
}
