package com.example.mori.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SignInRequest(
	@Email String email,
	@Size(min=10, max=128) String password
) {
}
