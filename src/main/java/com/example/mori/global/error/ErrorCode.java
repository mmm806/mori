package com.example.mori.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	// 공통
	INVALID_INPUT("C001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	VALIDATION_FAILED("C002", HttpStatus.BAD_REQUEST, "입력값 검증에 실패했습니다."),
	TYPE_MISMATCH("C003", HttpStatus.BAD_REQUEST, "파라미터 타입이 잘못되었습니다."),
	MISSING_PARAMETER("C004", HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
	PAYLOAD_MALFORMED("C005", HttpStatus.BAD_REQUEST, "요청 본문을 해석할 수 없습니다."),
	NOT_FOUND("C006", HttpStatus.NOT_FOUND, "요청 자원을 찾을 수 없습니다."),
	METHOD_NOT_ALLOWED("C007", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
	UNSUPPORTED_MEDIA_TYPE("C008", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),
	UNAUTHORIZED("C009", HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN("C010", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	CONFLICT("C011", HttpStatus.CONFLICT, "자원 충돌이 발생했습니다."),
	DATA_INTEGRITY_VIOLATION("C012", HttpStatus.CONFLICT, "데이터 무결성 위반입니다."),
	INTERNAL_ERROR("S001", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

	// 도메인 예시
	ENTITY_NOT_FOUND("B001", HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
	BUSINESS_RULE_VIOLATION("B002", HttpStatus.BAD_REQUEST, "비즈니스 규칙 위반입니다.");

	private final String code;
	private final HttpStatus status;
	private final String defaultMessage;

	ErrorCode(String code, HttpStatus status, String defaultMessage) {
		this.code = code;
		this.status = status;
		this.defaultMessage = defaultMessage;
	}
	public String code() { return code; }
	public HttpStatus status() { return status; }
	public String defaultMessage() { return defaultMessage; }
}