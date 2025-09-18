package com.example.mori.domain.user.mapper;

import org.mapstruct.*;

import com.example.mori.domain.user.dto.request.CreateUserRequest;
import com.example.mori.domain.user.dto.request.UpdateUserRequest;
import com.example.mori.domain.user.dto.response.UserResponseWithStats;
import com.example.mori.domain.user.dto.response.UserResponse;
import com.example.mori.domain.user.entity.User;
import com.example.mori.domain.user.entity.UserStats;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

	UserResponse toResponse(User user);


	@Named("toEntityWithHash")
	@Mapping(target = "id", ignore = true)
	default User toEntity(CreateUserRequest req, String pwHash) {
		return User.create(
			req.email().toLowerCase(),
			pwHash,
			req.nickname(),
			req.avatarUrl()
		);
	}

	/** 부분 수정 적용(null 무시) */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	default void apply(@MappingTarget User user, UpdateUserRequest req) {
		user.updateProfile(req.nickname(), req.avatarUrl());

	}


	default UserResponseWithStats toResponse(User user, UserStats userStats) {
		return new UserResponseWithStats(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getAvatarUrl(),
			user.getRole(),
			userStats != null ? userStats.getTotalEntries() : 0,
			userStats != null ? userStats.getCurrentStreak() : 0,
			userStats != null ? userStats.getLongestStreak() : 0
		);
	}
}