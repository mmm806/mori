package com.example.mori.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override public void addCorsMappings(CorsRegistry r) {
				r.addMapping("/api/**")
					.allowedOrigins("http://localhost:3000") // 러버블 미리보기나 로컬 프론트
					.allowedMethods("GET","POST","PATCH","DELETE","OPTIONS")
					.allowCredentials(true);
			}
		};
	}
}