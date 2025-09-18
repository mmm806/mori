package com.example.mori.domain.user.dto.request;

import java.util.UUID;

import javax.management.relation.Role;

import com.example.mori.global.enumType.UserRole;

public record UserSearchRequest(
	String cursor,
	UUID idAfter,
	Integer limit,
	String sortBy,
	String sortDirection,
	String emailLike,
	UserRole roleEqual
) {
}
