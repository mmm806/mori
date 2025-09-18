package com.example.mori.domain.diary.mapper;

import java.time.LocalDate;
import java.time.ZoneId;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.mori.domain.diary.dto.request.CreateDiaryRequest;
import com.example.mori.domain.diary.dto.request.UpdateDiaryRequest;
import com.example.mori.domain.diary.dto.response.DiaryResponse;
import com.example.mori.domain.diary.entity.Diary;
import com.example.mori.domain.user.entity.User;
import com.example.mori.global.enumType.DiaryVisibility;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DiaryMapper {

	/** 생성 매핑. 감사필드/삭제필드는 JPA가 처리하므로 무시 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", source = "user")
	@Mapping(target = "edited", constant = "false")
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Diary toEntity(CreateDiaryRequest req, User user);

	/** 응답 매핑 */
	DiaryResponse toResponse(Diary entity);

	/**
	 * 부분 수정. null은 무시. 값이 실제로 바뀌면 edited=true.
	 * MapStruct 기본 업데이트에 변경감지 플래그를 얹기 위해 default 메서드 사용.
	 */
	@BeanMapping(ignoreByDefault = true) // 명시 필드만 갱신
	default void update(Diary target, UpdateDiaryRequest req) {
		boolean changed = false;

		if (req.title() != null && !req.title().equals(target.getTitle())) {
			target.setTitle(req.title()); changed = true;
		}
		if (req.content() != null && !req.content().equals(target.getContent())) {
			target.setContent(req.content()); changed = true;
		}
		if (req.mood() != null && req.mood() != target.getMood()) {
			target.setMood(req.mood()); changed = true;
		}
		if (req.visibility() != null && req.visibility() != target.getVisibility()) {
			target.setVisibility(req.visibility()); changed = true;
		}
		if (changed) target.setEdited(true);
	}

	/** 생성 시 visibility 기본값 처리 */
	@AfterMapping
	default void applyDefaults(@MappingTarget Diary target, CreateDiaryRequest req) {
		if (target.getVisibility() == null) target.setVisibility(DiaryVisibility.PRIVATE);
		if (target.getEntryDate() == null) target.setEntryDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
	}
}
