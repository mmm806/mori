package com.example.mori.domain.user.dto.request;

import com.example.mori.global.enumType.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
	@NotNull UserRole role
) {
}
