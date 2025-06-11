package com.advanceauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.advanceauth.config.TOTPUtil;
import com.advanceauth.dto.LoginRequestDTO;
import com.advanceauth.dto.RegisterRequestDTO;
import com.advanceauth.entity.UserEntity;
import com.advanceauth.exception.UserAlreadyExistsException;
import com.advanceauth.repository.UserRepository;
import com.advanceauth.response.ApiResponse;
import com.advanceauth.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TOTPUtil totpUtil;
	private final JwtService jwtService;


//	public ApiResponse register(RegisterRequestDTO dto) {
//		if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
//			throw new UserAlreadyExistsException("Username already exists");
//		}
//
//		UserEntity user = UserEntity.builder().username(dto.getUsername())
//				.password(passwordEncoder.encode(dto.getPassword())).email(dto.getEmail()).mfaEnabled(true) // or set to
//																											// false and
//																											// allow
//																											// enabling
//																											// later
//				.build();
//
//		userRepository.save(user);
//		return new ApiResponse("User registered successfully", true);
//	}
	
	 public ApiResponse register(RegisterRequestDTO dto) {
	        var user = new UserEntity();
	        user.setUsername(dto.getUsername());
	        user.setEmail(dto.getEmail());
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	        user.setMfaEnabled(false);
	        userRepository.save(user);
	        return new ApiResponse("User registered", true);
	    }

	    public ApiResponse login(LoginRequestDTO dto) {
	        var user = userRepository.findByUsername(dto.getUsername())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
	            return new ApiResponse("Invalid password", false);
	        }

	        if (user.isMfaEnabled()) {
	            return new ApiResponse("MFA required", true);
	        }

	        String token = jwtService.generateToken(user.getUsername());
	        return new ApiResponse(token, true);
	    }
	public ApiResponse login(RegisterRequestDTO dto) {
	    if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
	        throw new UserAlreadyExistsException("Username already exists");
	    }

	    String secret = totpUtil.generateSecretKey();

	    UserEntity user = UserEntity.builder()
	            .username(dto.getUsername())
	            .password(passwordEncoder.encode(dto.getPassword()))
	            .email(dto.getEmail())
	            .mfaEnabled(true)
	            .mfaSecret(secret)
	            .build();

	    userRepository.save(user);

	    String qrUrl = totpUtil.getQRBarcodeURL(dto.getUsername(), secret);

	    return new ApiResponse("User registered successfully. Scan this QR in Google Authenticator: " + qrUrl, true);
	}

}
