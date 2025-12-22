package com.dormclean.dorm_cleaning_management.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 권한
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "0001", "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "0002", "접근 권한이 없습니다."),

    // 관리자
    SUPERADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "최고 관리자를 찾을 수 없습니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "관리자를 찾을 수 없습니다."),
    ADMIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "1003", "이미 존재하는 관리자입니다."),
    ADMIN_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "1004", "비밀번호가 일치하지 않습니다."),

    // 생활관
    DORM_NOT_FOUND(HttpStatus.NOT_FOUND, "1101", "생활관을 찾을 수 없습니다."),
    DORM_ALREADY_EXISTS(HttpStatus.CONFLICT, "1102", "이미 존재하는 생활관입니다."),

    // 호실
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "1201", "호실을 찾을 수 없습니다."),
    ROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "1202", "이미 존재하는 호실입니다."),
    INVALID_ROOM_STATUS(HttpStatus.BAD_REQUEST, "1203", "잘못된 호실 상태입니다."),
    CANNOT_CHANGE_ROOM_STATUS(HttpStatus.BAD_REQUEST, "1204", "호실 상태를 변경할 수 없습니다."),
    ROOM_CHECK_IN_NOT_ALLOWED(HttpStatus.CONFLICT, "1205", "준비된 방만 입실이 가능합니다."),
    ROOM_CHECK_OUT_NOT_ALLOWED(HttpStatus.CONFLICT, "1206", "재실 중인 방만 퇴실이 가능합니다."),
    ROOM_CLEAN_NOT_ALLOWED(HttpStatus.CONFLICT, "1207", "공실인 방만 청소가 가능합니다."),

    // QR
    QR_NOT_FOUND(HttpStatus.NOT_FOUND, "1301", "호실을 찾을 수 없습니다."),
    QR_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "1302", "QR 생성 중 오류가 발생했습니다."),
    INVALID_QR(HttpStatus.BAD_REQUEST, "1303", "유효하지 않은 QR입니다."),
    ZIP_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "1304", "ZIP 파일 생성 중 오류가 발생했습니다."),

    // 청소 인증 코드
    CLEANING_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "1401", "인증 코드를 찾을 수 없습니다."),
    INVALID_CLEANING_CODE(HttpStatus.BAD_REQUEST, "1402", "유효하지 않은 인증 코드입니다."),

    // Excel
    EXCEL_PROCESSING_FAILED(HttpStatus.BAD_REQUEST, "1501", "엑셀 파일 처리 중 오류가 발생했습니다."),

    // 기타
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "9001", "요청 값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 오류가 발생했습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 내부 에러 코드
    private final String message; // 사용자 메시지
}
