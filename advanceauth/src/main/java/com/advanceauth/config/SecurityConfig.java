//package com.advanceauth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(
//				auth -> auth.requestMatchers("/api/auth/**", "/oauth2/**").permitAll().anyRequest().authenticated())
//				.oauth2Login().and().csrf().disable();
//		return http.build();
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}

package com.advanceauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.advanceauth.repository.UserRepository;
import com.advanceauth.security.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler successHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf().disable()
          .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/oauth2/**", "/h2-console/**").permitAll()
            .requestMatchers("/api/student/**").authenticated()
            .anyRequest().authenticated()
          )
          .oauth2Login(oauth -> oauth
            .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
            .successHandler(successHandler)
          )
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
          .headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
