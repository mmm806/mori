package com.example.mori.domain.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mori.domain.user.dto.request.ChangePasswordRequest;
import com.example.mori.domain.user.dto.request.CreateUserRequest;
import com.example.mori.domain.user.dto.request.SignInRequest;
import com.example.mori.domain.user.dto.request.UpdateRoleRequest;
import com.example.mori.domain.user.dto.request.UpdateUserRequest;
import com.example.mori.domain.user.dto.request.UserSearchRequest;
import com.example.mori.domain.user.dto.response.UserResponseWithStats;
import com.example.mori.domain.user.dto.response.UserDtoCursorResponse;
import com.example.mori.domain.user.dto.response.UserResponse;
import com.example.mori.domain.user.entity.User;
import com.example.mori.domain.user.entity.UserStats;
import com.example.mori.domain.user.mapper.UserMapper;
import com.example.mori.domain.user.repository.UserRepository;
import com.example.mori.domain.user.repository.UserRepositoryCustom;
import com.example.mori.domain.user.repository.UserStatsRepositroy;
import com.example.mori.global.error.BusinessException;
import com.example.mori.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserStatsRepositroy userStatsRepositroy;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponse register(CreateUserRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new BusinessException(ErrorCode.USER_DUPLICATE_EMAIL, "email 중복");
		}
		if (userRepository.existsByNicknameIgnoreCase(request.nickname())) {
			throw new BusinessException(ErrorCode.USER_DUPLICATE_NICKNAME, "nickname 중복");
		}

		String pwHash = passwordEncoder.encode(request.password());
		User user = userMapper.toEntity(request, pwHash);

		userRepository.save(user);
		userStatsRepositroy.save(UserStats.init(user.getId()));
		return userMapper.toResponse(user);

	}

	@Transactional
	public UserResponse signIn(SignInRequest request) {
		User user = userRepository.findByEmailIgnoreCase(request.email())
			.orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

		if (!passwordEncoder.matches(request.password(), user.getPwHash())) {
			throw new BusinessException(ErrorCode.USER_PASSWORD_MISMATCH, "이메일 또는 비밀번호가 올바르지 않습니다.");
		}

		return userMapper.toResponse(user);
	}

	// 단건 조회
	@Transactional(readOnly = true)
	public UserResponseWithStats getById(UUID id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		UserStats userStats = userStatsRepositroy.findByUserId(user.getId())
			.orElse(null);
		return userMapper.toResponse(user, userStats);
	}

	@Transactional
	// 유저 정보 수정
	public UserResponseWithStats update(UUID id, UpdateUserRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (request.nickname() != null && !request.nickname().isBlank()) {
			if (userRepository.existsByNicknameIgnoreCaseAndIdNot(request.nickname(), user.getId())) {
				throw new BusinessException(ErrorCode.USER_DUPLICATE_NICKNAME, "닉네임 중복");
			}
			user.setNickname(request.nickname());
		}

		if (request.avatarUrl() != null) {
			user.setAvatarUrl(request.avatarUrl());
		}

		UserStats userStats = userStatsRepositroy.findByUserId(user.getId()).orElse(null);
		return userMapper.toResponse(user, userStats);
	}

	@Transactional
	public void changePassword(UUID id, ChangePasswordRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (request.currentPassword() == null ||
			!passwordEncoder.matches(request.currentPassword(), user.getPwHash())) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
			}

		String encodedPassword = passwordEncoder.encode(request.newPassword());
		user.changePasswordHash(encodedPassword);
	}

	@Transactional
	public void delete(UUID id) {
		if (!userRepository.existsById(id)) {
			throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "사용자를 찾을 수 없습니다.");
		}
		userRepository.deleteById(id);
	}

	// 역할: 커서 기반 사용자 검색
	@Transactional(readOnly = true)
	public UserDtoCursorResponse searchUsers(UserSearchRequest request) {

		// 0) 방어적 기본값
		int limit = (request.limit() == null || request.limit() <= 0) ? 20 : Math.min(request.limit(), 100);
		String sortBy = normalizeSortBy(request.sortBy());           // 허용 필드만
		String sortDir = normalizeSortDirection(request.sortDirection());

		// 1) 조건 기반 검색 쿼리 실행
		// 주의: repository.search(request) 가 limit/sort 값을 내부에서 쓰지 않으면
		//       아래와 같이 새 요청 객체로 전달해 일관 보장
		UserSearchRequest effective = new UserSearchRequest(
			request.cursor(),
			request.idAfter(),
			limit,
			sortBy,
			sortDir,
			request.emailLike(),
			request.roleEqual()
		);
		List<User> users = userRepository.search(effective);

		// 2) 데이터 변환
		List<UserResponseWithStats> dtos = users.stream()
			.map(UserResponseWithStats::from)  // 정적 팩토리 (User -> UserResponseWithStats)
			.toList();

		// 3) 커서 정보 계산
		UUID nextIdAfter = users.isEmpty() ? null : users.get(users.size() - 1).getId();
		boolean hasNext = users.size() == limit;
		String nextCursor = hasNext && nextIdAfter != null ? nextIdAfter.toString() : null;

		// 4) 총 개수 계산 (옵션)
		long totalCount = userRepository.count(effective);

		return new UserDtoCursorResponse(
			dtos,
			nextCursor,
			nextIdAfter,
			hasNext,
			totalCount,
			sortBy,
			sortDir
		);
	}

	/** 허용된 정렬 컬럼만 통과 */
	private String normalizeSortBy(String sortBy) {
		if (sortBy == null) return "createdAt";
		return switch (sortBy) {
			case "createdAt", "updatedAt", "email", "nickname", "id" -> sortBy;
			default -> "createdAt";
		};
	}

	/** ASC|DESC 이외는 기본 DESC */
	private String normalizeSortDirection(String dir) {
		if (dir == null) return "DESC";
		String v = dir.toUpperCase();
		return ("ASC".equals(v) ? "ASC" : "DESC");
	}

	public UserResponse updateUserRole(UUID id, UpdateRoleRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "사용자를 찾을 수 없습니다."));
		user.setRole(request.role());
		return userMapper.toResponse(user);
	}
}
