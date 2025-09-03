package com.example.mori.global.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;


import static com.example.mori.global.error.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/** 비즈니스 예외 처리 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
		ErrorCode ec = ex.getErrorCode();
		ApiError body = ApiError.of(
			ec, req.getRequestURI(), req.getMethod(), rid(), ex.getMessage(), ex.getErrors());
		log.warn("BusinessException: {} {}", ec.code(), ex.getMessage());
		return ResponseEntity.status(ec.status()).body(body);
	}

	/** @Valid 바디 검증 실패 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleInvalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
		List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
			.map(f -> ApiError.FieldError.of(f.getField(), f.getDefaultMessage(), f.getRejectedValue()))
			.toList();
		ApiError body = ApiError.of(VALIDATION_FAILED, req.getRequestURI(), req.getMethod(), rid(),
			"입력값 검증에 실패했습니다.", fields);
		log.debug("Validation failed: {}", fields);
		return ResponseEntity.status(VALIDATION_FAILED.status()).body(body);
	}

	/** 파라미터 검증 실패(@Validated + Constraint) */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
		List<ApiError.FieldError> fields = ex.getConstraintViolations().stream()
			.map(v -> ApiError.FieldError.of(
				v.getPropertyPath().toString(), v.getMessage(), v.getInvalidValue()))
			.toList();
		ApiError body = ApiError.of(VALIDATION_FAILED, req.getRequestURI(), req.getMethod(), rid(),
			"입력값 검증에 실패했습니다.", fields);
		return ResponseEntity.status(VALIDATION_FAILED.status()).body(body);
	}

	/** JSON 파싱 실패 등 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
		ApiError body = ApiError.of(PAYLOAD_MALFORMED, req.getRequestURI(), req.getMethod(), rid(),
			"요청 본문을 해석할 수 없습니다.", null);
		log.debug("Payload malformed", ex);
		return ResponseEntity.status(PAYLOAD_MALFORMED.status()).body(body);
	}

	/** 타입 변환 실패 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
		ApiError.FieldError fe = ApiError.FieldError.of(
			ex.getName(), "타입이 올바르지 않습니다.", ex.getValue());
		ApiError body = ApiError.of(TYPE_MISMATCH, req.getRequestURI(), req.getMethod(), rid(),
			"파라미터 타입이 잘못되었습니다.", List.of(fe));
		return ResponseEntity.status(TYPE_MISMATCH.status()).body(body);
	}

	/** 라우팅 미존재 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
		ApiError body = ApiError.of(NOT_FOUND, req.getRequestURI(), req.getMethod(), rid(),
			"요청 경로를 찾을 수 없습니다.", null);
		return ResponseEntity.status(NOT_FOUND.status()).body(body);
	}

	/** 메서드/미디어 타입 불일치 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
		ApiError body = ApiError.of(METHOD_NOT_ALLOWED, req.getRequestURI(), req.getMethod(), rid(), null, null);
		return ResponseEntity.status(METHOD_NOT_ALLOWED.status()).body(body);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiError> handleMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
		ApiError body = ApiError.of(UNSUPPORTED_MEDIA_TYPE, req.getRequestURI(), req.getMethod(), rid(), null, null);
		return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE.status()).body(body);
	}

	/** DB 제약 위반 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
		ApiError body = ApiError.of(DATA_INTEGRITY_VIOLATION, req.getRequestURI(), req.getMethod(), rid(),
			"중복 키 또는 제약조건 위반입니다.", null);
		log.warn("Data integrity violation", ex);
		return ResponseEntity.status(DATA_INTEGRITY_VIOLATION.status()).body(body);
	}

	/** 마지막 방어선 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleEtc(Exception ex, HttpServletRequest req) {
		ApiError body = ApiError.of(INTERNAL_ERROR, req.getRequestURI(), req.getMethod(), rid(), null, null);
		log.error("Unhandled exception", ex);
		return ResponseEntity.status(INTERNAL_ERROR.status()).body(body);
	}

	private String rid() { return MDC.get("requestId"); }
}
