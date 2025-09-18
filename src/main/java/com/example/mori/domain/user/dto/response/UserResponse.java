package com.example.mori.domain.user.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.mori.global.enumType.UserRole;

public record UserResponse(
	UUID id,
	String email,
	String nickname,
	String avatarUrl,
	UserRole role,
	Instant createdAt
) {
}
