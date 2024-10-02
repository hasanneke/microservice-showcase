package com.mentoapp.mentoauth.Service.AuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.mentoapp.mentoauth.Entity.People;
import com.mentoapp.mentoauth.Exception.Instances.ResourceAlreadyExistException;
import com.mentoapp.mentoauth.Exception.Instances.TokenExpired;
import com.mentoapp.mentoauth.Exception.Instances.UserNotFoundException;
import com.mentoapp.mentoauth.Repository.PeopleRepository;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.LoginUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.RegisterUserRequest;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.TokenResponse;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.UserResponse;
import com.mentoapp.mentoauth.Service.KafkaService.KafkaMessageService;
import com.mentoapp.mentoauth.Service.KafkaService.UserCreatedEvent;
import com.mentoapp.mentoauth.Service.Proxies.MentoProxy;
import com.mentoapp.mentoauth.Utility.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private MentoProxy mentoProxy;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    public TokenResponse refreshToken(String token) throws TokenExpired {
        boolean isExpired = jwtService.isTokenExpired(token);
        if(isExpired){
            throw new TokenExpired("Token expired");
        }
        String email = jwtService.extractEmail(token);
        ResponseEntity<UserResponse> response = mentoProxy.getUserByEmail(email);

        if (response.getStatusCode() == HttpStatus.OK) {
            UserResponse userResponse = response.getBody();
            assert userResponse != null;

            return jwtService.generateToken(userResponse.getEmail(), userResponse.getRoles());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public TokenResponse loginUserWithEmailAndPassword(LoginUserRequest loginUserRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword()));

        ResponseEntity<UserResponse> response = mentoProxy.getUserByEmail(loginUserRequest.getEmail());

        if (response.getStatusCode() == HttpStatus.OK) {
            UserResponse userResponse = response.getBody();
            assert userResponse != null;
            return jwtService.generateToken(userResponse.getEmail(), userResponse.getRoles());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public TokenResponse createUserWithEmailAndPassword(RegisterUserRequest registerUserRequest) throws JsonProcessingException {
        People checkUser = peopleRepository.findByEmail(registerUserRequest.getEmail()).orElse(null);

        if (checkUser != null) {
            throw new ResourceAlreadyExistException("User with provided email: " + registerUserRequest.getEmail() + " already exist");
        }

        ResponseEntity<UserResponse> response = mentoProxy.createUser(registerUserRequest);

        if (response.getStatusCode() == HttpStatus.OK) {
            peopleRepository.save(People.builder()
                    .commonName(registerUserRequest.getEmail())
                    .email(registerUserRequest.getEmail())
                    .password(registerUserRequest.getPassword())
                    .surname(registerUserRequest.getSurname())
                    .build());
            UserResponse userResponse = response.getBody();
            assert userResponse != null;
            kafkaMessageService.sendUserCreatedEvent(UserCreatedEvent.builder()
                    .id(userResponse.getId())
                    .email(userResponse.getEmail())
                    .name(userResponse.getFullName())
                    .build());
            return jwtService.generateToken(userResponse.getEmail(), userResponse.getRoles());
        } else {
            throw new ResourceAlreadyExistException("User already exist");
        }
    }

    public TokenResponse googleLogin(String accessToken) throws TokenVerifier.VerificationException, JsonProcessingException {
        System.out.println("OAuth2 Request Received");
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();

        JsonWebSignature jsonWebSignature = tokenVerifier.verify(accessToken);
        String email = (String) jsonWebSignature.getPayload().get("email");
        String givenName = (String) jsonWebSignature.getPayload().get("name");
        String familyName = (String) jsonWebSignature.getPayload().get("family_name");

        Optional<UserResponse> userResponse = checkUserExist(email);

        if (userResponse.isPresent()) {
            return jwtService.generateToken(userResponse.get().getEmail(), userResponse.get().getRoles());
        } else {
            ResponseEntity<UserResponse> createResponse = mentoProxy.createUser(RegisterUserRequest.builder()
                    .name(givenName)
                    .surname(familyName)
                    .email(email)
                    .build());
            UserResponse createdUser = createResponse.getBody();
            kafkaMessageService.sendUserCreatedEvent(
                    UserCreatedEvent.builder()
                            .id(createdUser.getId())
                            .email(createdUser.getEmail())
                            .name(createdUser.getFullName())
                            .build()
            );
            return jwtService.generateToken(createResponse.getBody().getEmail(), createResponse.getBody().getRoles());
        }
    }

    private Optional<UserResponse> checkUserExist(String email){
        try{
            ResponseEntity<UserResponse> response = mentoProxy.getUserByEmail(email);
            return Optional.ofNullable(response.getBody());
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
