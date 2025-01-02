package com.akhil.linkedin.user_service.services;

import com.akhil.linkedin.user_service.dto.request.LoginRequestDTO;
import com.akhil.linkedin.user_service.dto.request.SignupRequestDTO;
import com.akhil.linkedin.user_service.dto.response.LoginResponseDTO;
import com.akhil.linkedin.user_service.dto.response.SignupResponseDTO;

public interface AuthService {

    SignupResponseDTO signup(SignupRequestDTO signupRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
