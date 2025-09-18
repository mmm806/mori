package com.example.mori.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
	@Size(min=2, max=50) String nickname,
	@Pattern(regexp="^https?://.*", message="URL 형식") String avatarUrl
) {
}
