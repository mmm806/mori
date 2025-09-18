package com.example.mori.domain.diary.dto.request;

import com.example.mori.global.enumType.DailyMood;
import com.example.mori.global.enumType.DiaryVisibility;

import jakarta.validation.constraints.Size;

public record UpdateDiaryRequest(
	@Size(max=120) String title,
	@Size(max=5000) String content,
	DailyMood mood,
	DiaryVisibility visibility
) {
}
