package com.example.mori.domain.diary.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mori.domain.diary.dto.request.CreateDiaryRequest;
import com.example.mori.domain.diary.dto.request.UpdateDiaryRequest;
import com.example.mori.domain.diary.dto.response.DiaryResponse;
import com.example.mori.domain.diary.entity.Diary;
import com.example.mori.domain.diary.mapper.DiaryMapper;
import com.example.mori.domain.diary.repository.DiaryRepository;
import com.example.mori.domain.user.entity.User;
import com.example.mori.domain.user.repository.UserRepository;
import com.example.mori.global.error.BusinessException;
import com.example.mori.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryRepository diaryRepository;
	private final UserRepository userRepository;
	private final DiaryMapper diaryMapper;

	@Transactional
	public DiaryResponse create(UUID userId, CreateDiaryRequest request) {
		if (diaryRepository.existsByUser_IdAndEntryDate(userId, request.entryDate())){
			throw new BusinessException(ErrorCode.ENTITY_CONFLICT, "해당 날짜의 일기가 이미 존재합니다.");
		}
		User user = userRepository.getReferenceById(userId);
		Diary diary = diaryRepository.save(diaryMapper.toEntity(request, user));
		return diaryMapper.toResponse(diary);
	}

	@Transactional
	public DiaryResponse getById(UUID userId, UUID id) {
		Diary diary = diaryRepository.findByIdAndUser_Id(id, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "일기를 찾을 수 없습니다."));
		return diaryMapper.toResponse(diary);
	}


	@Transactional
	public DiaryResponse update(UUID userId, UUID id, UpdateDiaryRequest request) {
		Diary diary = diaryRepository.findByIdAndUser_Id(id, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "일기를 찾을 수 없습니다."));
		diaryMapper.update(diary, request);
		return diaryMapper.toResponse(diary);
	}

	public void delete(UUID userId, UUID id) {
		Diary diary = diaryRepository.findByIdAndUser_Id(id, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "일기를 찾을 수 없습니다."));
		diaryRepository.delete(diary);
	}

	public DiaryResponse getByUserAndDate(UUID userId, LocalDate date) {
		Diary diary = diaryRepository.findByUserIdAndEntryDate(userId, date)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "일기를 찾을 수 없습니다."));

		return diaryMapper.toResponse(diary);
	}
}
