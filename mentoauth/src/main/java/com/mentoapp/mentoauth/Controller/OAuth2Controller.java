package com.mentoapp.mentoauth.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.TokenVerifier;
import com.mentoapp.mentoauth.Service.AuthService.AuthService;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.LoginUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/google")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponse> login(@RequestParam(name = "accessToken") String accessToken) throws TokenVerifier.VerificationException, JsonProcessingException {
        TokenResponse tokenResponse = authService.googleLogin(accessToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
