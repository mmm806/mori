package com.example.mori.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mori.domain.user.dto.request.UserSearchRequest;
import com.example.mori.domain.user.entity.User;
import com.example.mori.global.enumType.UserRole;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {
	boolean existsByEmailIgnoreCase(String email);
	boolean existsByNicknameIgnoreCase(String nickname);
	Optional<User> findById(UUID id);
	Optional<User> findByEmailIgnoreCase(String email);
	User findByNicknameIgnoreCase(String nickname);

	// 닉네임 중복 체크(본인 제외)
	boolean existsByNicknameIgnoreCaseAndIdNot(String nickname, UUID id);

	@Query("""
        select u from User u
        where (:q is null or lower(u.email) like lower(concat('%',:q,'%'))
               or lower(u.nickname) like lower(concat('%',:q,'%')))
          and (:role is null or u.role = :role)
        """)
	Page<User> search(@Param("q") String q,
		@Param("role") UserRole role,
		Pageable pageable);

}
