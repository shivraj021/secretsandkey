package com.advanceauth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfaVerifyDTO {
	private String username;
	private int code;
}
