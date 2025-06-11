package com.advanceauth.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	private String message;
	private boolean success;
}
