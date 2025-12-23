package com.dormclean.dorm_cleaning_management.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dormclean.dorm_cleaning_management.dto.exception.ErrorResponseDto;
import com.dormclean.dorm_cleaning_management.entity.enums.ErrorCode;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.dormclean.dorm_cleaning_management")
public class GlobalExceptionHandler {
    // @Valid 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .badRequest()
                .body(Map.of("message", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handle(BaseException e) {

        // 서버 로그에만 code, status 기록
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[ErrorCode: {}] {}",
                errorCode.getCode(),
                errorCode.getMessage());

        // 사용자에게는 message만 전달
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponseDto.from(e));
    }
}