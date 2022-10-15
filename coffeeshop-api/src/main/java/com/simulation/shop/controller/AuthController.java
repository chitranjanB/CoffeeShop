package com.simulation.shop.controller;

import com.coffee.shared.request.APIResponse;
import com.coffee.shared.request.AuthRequest;
import com.coffee.shared.request.RegisterRequest;
import com.simulation.shop.service.AuthService;
import com.simulation.shop.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody RegisterRequest registerRequest) {
        APIResponse apiResponse = authService.register(registerRequest);
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<APIResponse> authenticate(@RequestBody AuthRequest authRequest) {
        APIResponse apiResponse = authService.authenticate(authRequest);
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }
}
