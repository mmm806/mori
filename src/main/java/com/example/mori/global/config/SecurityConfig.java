package com.example.mori.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.cors(Customizer.withDefaults())
			.formLogin(AbstractHttpConfigurer::disable)   // 스프링 기본 /login 비활성화
			.httpBasic(AbstractHttpConfigurer::disable)   // 기본 인증 비활성화
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				// SPA/정적 리소스 허용
				.requestMatchers("/", "/index.html", "/login", "/signup",
					"/favicon.ico", "/assets/**", "/static/**", "/_next/**", "/images/**",
					"/.well-known/**").permitAll()
				// 회원가입, 로그인 API 허용
				.requestMatchers(HttpMethod.POST, "/api/users", "/api/users/login", "api/diaries").permitAll()
				// 필요 시 공개 API 추가
				//.requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
				.anyRequest().permitAll()
			)
			.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }
}
