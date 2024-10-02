package com.mentoapp.mentoapp.Service.UserService;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.UserNotFoundException;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.UserService.DTOs.MentorResponse;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Utility.CustomMapper;
import com.mentoapp.mentoapp.Utility.UserMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // TODO: return UserResponse
    public List<User> getUser(){
        return userRepository.findAll();
    }
    public UserResponse getUserOne(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));
        return CustomMapper.userToUserResponse(user);
    }

    public UserResponse getMe(){
        return UserMapper.INSTANCE.userToUserResponse(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new UserNotFoundException("User not found")));
    }
}
