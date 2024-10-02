package com.mentoapp.mentoauth.Service.Proxies;

import com.mentoapp.mentoauth.Service.AuthService.DTOs.RegisterUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mentoapp")
public interface MentoProxy {
    @GetMapping("api/v1/public/users/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable(name = "email") String email);

    @PostMapping("api/v1/public/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody RegisterUserRequest request);
}
