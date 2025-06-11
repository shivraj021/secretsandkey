package com.advanceauth.security;

import com.advanceauth.entity.UserEntity;
import com.advanceauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) {
		OAuth2User oAuth2User = super.loadUser(request);

		String email = oAuth2User.getAttribute("email");

		userRepository.findByUsername(email).orElseGet(() -> {
			UserEntity newUser = new UserEntity();
			newUser.setUsername(email);
			newUser.setEmail(email);
			newUser.setPassword("oauth_user");
			newUser.setMfaEnabled(false);
			return userRepository.save(newUser);
		});

		return oAuth2User;
	}
}
