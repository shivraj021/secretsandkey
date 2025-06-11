package com.advanceauth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
	private String username;
	private String password;
	private String email;
}
