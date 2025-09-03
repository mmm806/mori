package com.example.mori.global.error;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.Instant;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record ApiError(
	String code,            // 에러 식별 코드(e.g. C001)
	int status,             // HTTP 상태 코드(e.g. 400)
	String message,         // 사용자 메시지(국소/기본)
	String path,            // 요청 경로
	String method,          // 요청 메서드
	String requestId,       // 요청 추적 ID(X-Request-Id)
	List<FieldError> errors,// 필드 오류 목록(검증 실패 시)
	Instant timestamp       // 발생 시각(UTC)
) {
	/** ErrorCode + 컨텍스트로 ApiError 생성(메시지 null/blank이면 기본 메시지 사용) */
	public static ApiError of(
		ErrorCode ec,
		String path,
		String method,
		String requestId,
		String message,
		List<FieldError> errors
	) {
		return new ApiError(
			ec.code(),
			ec.status().value(),
			(message == null || message.isBlank()) ? ec.defaultMessage() : message,
			path,
			method,
			requestId,
			(errors == null || errors.isEmpty()) ? null : errors,
			Instant.now()
		);
	}

	/** 단일 필드 오류 표현용 DTO */
	public record FieldError(
		String field,        // 필드명
		String reason,       // 사유/메시지
		Object rejectedValue // 거부된 값(있으면)
	) {
		public static FieldError of(String field, String reason, Object rejectedValue) {
			return new FieldError(field, reason, rejectedValue);
		}
	}
}