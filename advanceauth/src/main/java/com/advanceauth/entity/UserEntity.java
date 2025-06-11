package com.advanceauth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String email;
	private boolean mfaEnabled;
	private String mfaSecret;

	private String role; // e.g., ROLE_USER, ROLE_ADMIN
}
