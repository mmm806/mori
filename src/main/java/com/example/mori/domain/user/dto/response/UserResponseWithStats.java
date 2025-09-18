package com.example.mori.domain.user.dto.response;

import java.util.UUID;

import com.example.mori.domain.user.entity.User;
import com.example.mori.domain.user.entity.UserStats;
import com.example.mori.global.enumType.UserRole;

public record UserResponseWithStats(
	UUID id,
	String email,        // 비공개가 필요하면 /{id}에서는 null 처리 가능
	String nickname,
	String avatarUrl,
	UserRole role,
	int totalEntries,
	int currentStreak,
	int longestStreak
) {

	/** User + UserStats -> DTO */
	public static UserResponseWithStats from(User u, UserStats s) {
		return new UserResponseWithStats(
			u.getId(),
			u.getEmail(),
			u.getNickname(),
			u.getAvatarUrl(),
			u.getRole(),
			s != null ? s.getTotalEntries() : 0,
			s != null ? s.getCurrentStreak() : 0,
			s != null ? s.getLongestStreak() : 0
		);
	}
	/** 통계 미로딩 시 기본값 0 적용 */
	public static UserResponseWithStats from(User u) {
		return from(u, null);
	}
}
