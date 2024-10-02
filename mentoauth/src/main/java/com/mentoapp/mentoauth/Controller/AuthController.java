package com.mentoapp.mentoauth.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoauth.Exception.Instances.TokenExpired;
import com.mentoapp.mentoauth.Service.AuthService.AuthService;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.LoginUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.RegisterUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refreshToken(@RequestParam(name = "token") String token) throws TokenExpired {
        return authService.refreshToken(token);
    }

    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponse> login(@Validated @RequestBody LoginUserRequest loginUserRequest){
        TokenResponse tokenResponse = authService.loginUserWithEmailAndPassword(loginUserRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponse> register(@Validated @RequestBody RegisterUserRequest registerUserRequest) throws JsonProcessingException {
        TokenResponse tokenResponse = authService.createUserWithEmailAndPassword(registerUserRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}