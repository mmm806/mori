package com.example.mori.domain.diary.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mori.domain.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, UUID> {

	boolean existsByUser_IdAndEntryDate(UUID userId, LocalDate entryDate);

	Optional<Diary> findByIdAndUser_Id(UUID id, UUID userId);

	Optional<Object> findByUserIdAndEntryDateAndDeletedAtIsNull(UUID userId, LocalDate date);

	Optional<Diary> findByUserIdAndEntryDate(UUID userId, LocalDate date);
}
