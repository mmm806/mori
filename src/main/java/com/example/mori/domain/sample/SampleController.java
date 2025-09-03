package com.example.mori.domain.sample;

import com.example.mori.global.error.BusinessException;
import com.example.mori.global.error.ErrorCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/samples")
public class SampleController {

	/** 요청 파라미터 검증 예시(@Validated + ConstraintViolationException) */
	@GetMapping("/{id}")
	public String get(@PathVariable @Min(1) long id) {
		if (id == 404) throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "샘플을 찾을 수 없습니다.");
		return "ok";
	}

	/** 바디 검증 예시(MethodArgumentNotValidException) */
	@PostMapping
	public String create(@RequestBody @jakarta.validation.Valid CreateSample req) { return "created"; }


	public record CreateSample(
		@NotBlank String title
	) { /* DTO 필드 검증 */ }
}
