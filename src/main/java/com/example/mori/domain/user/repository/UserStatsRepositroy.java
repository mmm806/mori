package com.example.mori.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mori.domain.user.entity.UserStats;

public interface UserStatsRepositroy extends JpaRepository<UserStats, UUID> {
	Optional<UserStats> findByUserId(UUID userId);
}
