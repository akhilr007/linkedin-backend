package com.akhil.linkedin.user_service.services.impl;

import com.akhil.linkedin.user_service.dto.request.LoginRequestDTO;
import com.akhil.linkedin.user_service.dto.request.SignupRequestDTO;
import com.akhil.linkedin.user_service.dto.response.LoginResponseDTO;
import com.akhil.linkedin.user_service.dto.response.SignupResponseDTO;
import com.akhil.linkedin.user_service.entities.User;
import com.akhil.linkedin.user_service.exceptions.EmailAlreadyInUseException;
import com.akhil.linkedin.user_service.exceptions.InvalidCredentialsException;
import com.akhil.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.user_service.kafka.events.UserSignupEvent;
import com.akhil.linkedin.user_service.repositories.UserRepository;
import com.akhil.linkedin.user_service.services.AuthService;
import com.akhil.linkedin.user_service.utils.JwtUtil;
import com.akhil.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final KafkaTemplate<Long, UserSignupEvent> userSignupEventKafkaTemplate;

    @Override
    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO) {

        log.info("Signup request received: {}", signupRequestDTO);

        // Check if email is already in use
        if (userRepository.existsByEmail(signupRequestDTO.getEmail())) {
            log.error("Email already in use: {}", signupRequestDTO.getEmail());
            throw new EmailAlreadyInUseException("Email already in use");
        }

        // Convert DTO to Entity
        User user = modelMapper.map(signupRequestDTO, User.class);
        user.setPassword(PasswordUtil.encode(signupRequestDTO.getPassword()));

        // Save user to DB
        User savedUser = userRepository.save(user);

        UserSignupEvent userSignupEvent = UserSignupEvent
                .builder()
                .email(savedUser.getEmail())
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .build();

        userSignupEventKafkaTemplate.send("user-signup-topic", userSignupEvent);
        log.info("User signup topic published for user-signup-topic: {}", userSignupEvent);

        SignupResponseDTO response =  SignupResponseDTO.builder()
                .success(true)
                .message("User created successfully")
                .build();

        log.info("Signup response sent: {}", response);
        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        log.info("Login request received: {}", loginRequestDTO);

        // Find user by email
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", loginRequestDTO.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });

        // Check if password is correct
        if (!PasswordUtil.match(loginRequestDTO.getPassword(), user.getPassword())) {
            log.error("Invalid password for user: {}", loginRequestDTO.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Generate token
        String token = jwtUtil.generateAccessToken(user);

        LoginResponseDTO response = LoginResponseDTO.builder()
                .success(true)
                .token(token)
                .message("Login successful")
                .build();

        log.info("Login response sent: {}", response);
        return response;
    }


}
