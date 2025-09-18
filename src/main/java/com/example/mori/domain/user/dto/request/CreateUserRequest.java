package com.example.mori.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
	@Email String email,
	@Size(min=10, max=128) String password,
	@Size(min=2, max=50) String nickname,
	@Pattern(regexp="^https?://.*", message="URL 형식") String avatarUrl
) {
}
