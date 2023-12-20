package com.example.withdogandcat.global.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 성공 (2000번대)
     */
    SUCCESS(true, 2000, "요청에 성공하였습니다."),

    /**
     * 클라이언트 오류 (4000번대)
     */
    // 입력 및 요청 관련
    BAD_REQUEST(false, 4000, "잘못된 요청입니다."),
    INVALID_INPUT(false, 4001, "입력값을 확인해주세요."),
    INVALID_API_URL(false, 4002, "잘못된 API URL입니다."),
    OUT_OF_RANGE(false, 4003, "요청한 페이지 범위가 적절하지 않습니다."),
    ELEMENTS_IS_REQUIRED(false, 4004, "필수 입력 필드가 누락되었습니다."),

    // 인증 및 권한 관련
    INVALID_VERIFICATION_CODE(false, 4100, "이메일 인증 실패, 이메일과 인증코드를 다시 한번 확인해 주세요."),
    EMAIL_NOT_FOUND(false, 4101, "이메일 인증을 해주세요."),
    TOKEN_NOT_EXIST(false, 4102, "이 기능을 사용하기 위해서는 로그인이 필요합니다."),
    EXPIRED_TOKEN(false, 4103, "토큰이 만료되었습니다."),
    INVALID_TOKEN(false, 4104, "토큰이 유효하지 않습니다."),
    ACCESS_DENIED(false, 4105, "접근 권한이 없습니다."),
    NOT_FOUND_TOKEN(false, 4106, "토큰을 찾을 수 없습니다."),

    // 접근 및 상태 관련
    EMAIL_DIFFERENT_FORMAT(false, 4200, "이메일 형식이 올바르지 않습니다."),
    PASSWORD_DIFFERENT_FORMAT(false, 4201, "비밀번호는 8~15자리로, 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."),
    EMAIL_ALREADY_EXISTS(false, 4202, "이미 등록된 이메일입니다."),
    INVALID_EMAIL_PASSWORD(false, 4203, "이메일 또는 비밀번호가 정확하지 않습니다."),
    PASSWORD_MISMATCH(false, 4204, "비밀번호가 일치하지 않습니다."),
    LIKE_NOT_FOUND(false, 4205, "추천 기록이 없습니다."),
    ALREADY_LIKED(false, 4206, "이미 추천 하셨습니다."),
    LOGIN_FAILURE(false, 4207, "로그인 실패"),

    // 등록 실패 (4500번대)
    REGISTRATION_FAILED(false, 4500, "등록에 실패하였습니다."),
    USER_REGISTRATION_FAILED(false, 4501, "유저 등록에 실패하였습니다."),
    POST_REGISTRATION_FAILED(false, 4502, "게시물 등록에 실패하였습니다."),
    IMAGE_UPLOAD_FAILED(false, 4503, "이미지 업로드에 실패하였습니다."),

    // 조회 실패 (4600번대)
    RETRIEVAL_FAILED(false, 4600, "조회에 실패하였습니다."),
    USER_NOT_FOUND(false, 4601, "유저를 찾을 수 없습니다."),
    SHOP_NOT_FOUND(false,4602,"가게를 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(false, 4603, "채팅방을 찾을 수 없습니다."),
    PET_NOT_FOUND(false,4604,"반려동물을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(false, 4607, "후기를 찾을 수 없습니다."),

    /**
     * 서버 오류 (5000번대)
     */
    SERVER_ERROR(false, 5000, "서버와의 연결에 실패하였습니다."),
    CONNECTION_FAILED(false, 5001, "서버 연결 실패"),
    SERVER_RESPONSE_ERROR(false, 5002, "서버 응답 에러"),
    UNEXPECTED_ERROR(false, 5003, "예상치 못한 오류가 발생했습니다."),

    // 데이터베이스 및 캐시 관련 오류 (5100번대)
    DATABASE_ERROR(false, 5100, "데이터베이스 연결에 실패하였습니다."),
    REDIS_ERROR(false, 5101, "Redis 연결에 실패하였습니다."),

    // 인증 및 토큰 관련 오류 (5200번대)
    EXPIRED_REFRESH_TOKEN(false, 5200, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(false, 5201, "지원되지 않는 JWT 토큰 입니다."),
    NOT_EXIST_REFRESH_JWT(false, 5202, "리프레시 토큰이 존재하지 않습니다."),
    AUTHENTICATION_FAILED(false, 5203, "인증 오류"),

    // 기타 서버 오류 (5300번대)
    FAIL_TO_ENCODING(false, 5300, "요청 인코딩 실패"),
    FAIL_TO_JSON(false, 5301, "JSON 파싱 오류");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
