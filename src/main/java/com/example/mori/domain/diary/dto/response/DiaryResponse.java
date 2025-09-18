package com.example.mori.domain.diary.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.example.mori.global.enumType.DailyMood;
import com.example.mori.global.enumType.DiaryVisibility;

public record DiaryResponse(
	UUID id,
	LocalDate entryDate,
	String title,
	String content,
	DailyMood mood,
	DiaryVisibility visibility,
	boolean edited,
	Instant createdAt,
	Instant updatedAt
) {
}
