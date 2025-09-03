package com.example.mori.global.error;

import java.util.List;

/** 서비스/도메인 레이어에서 던지는 표준 런타임 예외 */
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;
	private final List<ApiError.FieldError> errors;

	public BusinessException(ErrorCode errorCode) {
		this(errorCode, errorCode.defaultMessage(), null);
	}
	public BusinessException(ErrorCode errorCode, String message) {
		this(errorCode, message, null);
	}
	public BusinessException(ErrorCode errorCode, String message, List<ApiError.FieldError> errors) {
		super(message);
		this.errorCode = errorCode;
		this.errors = errors;
	}
	public ErrorCode getErrorCode() { return errorCode; }
	public List<ApiError.FieldError> getErrors() { return errors; }
}
