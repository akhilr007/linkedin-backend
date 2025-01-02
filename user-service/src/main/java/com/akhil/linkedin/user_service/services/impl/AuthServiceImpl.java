package com.akhil.linkedin.user_service.services.impl;

import com.akhil.linkedin.user_service.dto.request.SignupRequestDTO;
import com.akhil.linkedin.user_service.dto.response.SignupResponseDTO;
import com.akhil.linkedin.user_service.entities.User;
import com.akhil.linkedin.user_service.exceptions.EmailAlreadyInUseException;
import com.akhil.linkedin.user_service.repositories.UserRepository;
import com.akhil.linkedin.user_service.services.AuthService;
import com.akhil.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
        userRepository.save(user);

        SignupResponseDTO response =  SignupResponseDTO.builder()
                .success(true)
                .message("User created successfully")
                .build();

        log.info("Signup response sent: {}", response);
        return response;
    }
}
