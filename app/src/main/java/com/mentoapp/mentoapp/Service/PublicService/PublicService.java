package com.mentoapp.mentoapp.Service.PublicService;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.ResourceAlreadyExistException;
import com.mentoapp.mentoapp.Exception.Instance.UserNotFoundException;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.AuthService.DTOs.CreateUserRequest;
import com.mentoapp.mentoapp.Service.UserService.DTOs.UserResponse;
import com.mentoapp.mentoapp.Utility.UserMapper;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class PublicService {

    static final Logger logger = LoggerFactory.getLogger(PublicService.class);


    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserWithEmail(String email) {
        logger.info(String.format("Get user by email request received -> %s", email));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));
        return UserMapper.INSTANCE.userToUserResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
        User checkUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (checkUser != null){
            throw new ResourceAlreadyExistException("User already exist");
        }

        User user = userRepository.save(User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .surname(request.getSurname())
                .fullName(request.getName() + " " + request.getSurname())
                .roles(Set.of(UserRole.mentee))
                .website(request.getWebsite())
                .linkedIn(request.getLinkedIn())
                .reviewCount(0)
                .reviewScore(0.0)
                .sessionCount(0)
                .build());

        return UserMapper.INSTANCE.userToUserResponse(user);
    }
}
