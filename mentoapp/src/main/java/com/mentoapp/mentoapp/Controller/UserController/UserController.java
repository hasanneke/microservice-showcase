package com.mentoapp.mentoapp.Controller.UserController;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Service.MentorSessionService.DTOs.SessionResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserOne(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(userService.getUserOne(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}
