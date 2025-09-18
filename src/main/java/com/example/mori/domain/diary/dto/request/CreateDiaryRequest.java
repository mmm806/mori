package com.example.mori.domain.diary.dto.request;

import java.time.LocalDate;

import com.example.mori.global.enumType.DailyMood;
import com.example.mori.global.enumType.DiaryVisibility;
import com.example.mori.global.jackson.LocalDateFlexDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDiaryRequest(
	@NotBlank @Size(max=120) String title,
	@NotBlank @Size(max=5000) String content,
	DailyMood mood,                       // null 허용
	@JsonAlias({"entryDate","entry_date","diaryDate","diary_date","date","selectedDate","selected_date"})
	@JsonDeserialize(using = LocalDateFlexDeserializer.class)
	LocalDate entryDate,
	DiaryVisibility visibility
) {
}
