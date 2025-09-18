package com.example.mori.domain.diary.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mori.domain.diary.dto.request.CreateDiaryRequest;
import com.example.mori.domain.diary.dto.request.UpdateDiaryRequest;
import com.example.mori.domain.diary.dto.response.DiaryResponse;
import com.example.mori.domain.diary.service.DiaryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping
	public ResponseEntity<DiaryResponse> create(
		@RequestHeader("X-USER-ID") UUID userId, // 추후 수정 요망
		@Valid @RequestBody CreateDiaryRequest request
	){
		DiaryResponse response = diaryService.create(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DiaryResponse> getById(
		@RequestHeader("X-USER-ID") UUID userId, // 추후 수정 요망
		@PathVariable UUID id
	) {
		DiaryResponse response = diaryService.getById(userId, id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<DiaryResponse> update(
		@RequestHeader("X-USER-ID") UUID userId, // 추후 수정 요망
		@PathVariable UUID id,
		@Valid @RequestBody UpdateDiaryRequest request
	) {
		DiaryResponse response = diaryService.update(userId, id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
		@RequestHeader("X-USER-ID") UUID userId, // 추후 수정 요망
		@PathVariable UUID id
	) {
		diaryService.delete(userId, id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<DiaryResponse> getByDate(
		@AuthenticationPrincipal(expression = "id") UUID userId, // 보안 컨텍스트에서 현재 사용자 ID 주입
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date // YYYY-MM-DD 형식 강제
	) {
		DiaryResponse response = diaryService.getByUserAndDate(userId, date);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}


}
