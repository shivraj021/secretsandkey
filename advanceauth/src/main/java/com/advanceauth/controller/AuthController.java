package com.advanceauth.controller;

import com.advanceauth.config.TOTPUtil;
import com.advanceauth.dto.LoginRequestDTO;
import com.advanceauth.dto.MfaVerifyDTO;
import com.advanceauth.dto.RegisterRequestDTO;
import com.advanceauth.repository.UserRepository;
import com.advanceauth.response.ApiResponse;
import com.advanceauth.security.JwtService;
import com.advanceauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final TOTPUtil totpUtil;
	private final UserRepository userRepo;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ApiResponse login(@RequestBody LoginRequestDTO dto) {
		return authService.login(dto);
	}

	@PostMapping("/register")
	public ApiResponse register(@RequestBody RegisterRequestDTO dto) {
		var resp = authService.register(dto);
		return resp;
	}

	@PostMapping("/get-mfa")
	public ApiResponse getMfa(@RequestParam String username) {
		var user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		String secret = totpUtil.generateSecretKey();
		user.setMfaSecret(secret);
		user.setMfaEnabled(true);
		userRepo.save(user);
		return new ApiResponse(totpUtil.getQRBarcodeURL(username, secret), true);
	}

	@PostMapping("/verify-mfa")
	public ApiResponse verify(@RequestBody MfaVerifyDTO dto) {
		var user = userRepo.findByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
		if (totpUtil.verifyCode(user.getMfaSecret(), dto.getCode())) {
			String token = jwtService.generateToken(dto.getUsername());
			return new ApiResponse(token, true);
		}
		return new ApiResponse("Invalid code", false);
	}
}
