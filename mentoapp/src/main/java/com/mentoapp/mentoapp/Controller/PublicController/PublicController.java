package com.mentoapp.mentoapp.Controller.PublicController;

import com.mentoapp.mentoapp.Service.AuthService.DTOs.CreateUserRequest;
import com.mentoapp.mentoapp.Service.PublicService.PublicService;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/public")
@AllArgsConstructor
@RequiredArgsConstructor
public class PublicController {
    @Autowired
    private PublicService publicService;

    @GetMapping(path = "/users/{email}")
    ResponseEntity<UserResponse> getUserByEmail(@PathVariable(name = "email") String email) {
        UserResponse userResponse = publicService.getUserWithEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping(path = "/users")
    ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(publicService.createUser(request));
    }
}
