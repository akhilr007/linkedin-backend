package com.akhil.linkedin.user_service.controllers;

import com.akhil.linkedin.user_service.dto.request.SignupRequestDTO;
import com.akhil.linkedin.user_service.dto.response.SignupResponseDTO;
import com.akhil.linkedin.user_service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        log.info("Signup request received: {}", signupRequestDTO);
        SignupResponseDTO response = authService.signup(signupRequestDTO);

        log.info("Signup response sent: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
