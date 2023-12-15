package com.example.withdogandcat.global.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    // TODO 에러처리 통합 + 정리 하기

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_ACCESS_JWT(false, 2001, "Access 토큰을 입력해주세요."),
    EMPTY_REFRESH_JWT(false, 2002, "Refresh 토큰을 입력해주세요."),
    INVALID_JWT(false, 2003, "지원되지 않거나 잘못된 토큰 입니다."),
    NOT_EXIST_REFRESH_JWT(false,2005,"존재하지 않거나 만료된 Refresh 토큰입니다. 다시 로그인해주세요."),
    EXPIRED_JWT(false,2006,"만료된 Access 토큰입니다. Refresh 토큰을 이용해서 새로운 Access 토큰을 발급 받으세요."),

    // users
    USERS_EMPTY_USER_ID(false, 2101, "유저 아이디 값을 확인해주세요."),
    INVALID_USER_CRAWLING(false, 2103, "유저 정보를 불러오는데 실패했습니다"),
    INVALID_USER_PW(false, 2107, "비밀번호를 확인해주세요."),
    INVALID_USER_ID(false, 2108, "유저 ID를 확인해주세요."),
    INVALID_CHATROOM(false, 2201, "채팅룸 ID를 확인해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    REDIS_ERROR(false, 4000, "redis 연결에 실패하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public static BaseResponseStatus of(final String errorName){
        return BaseResponseStatus.valueOf(errorName);
    }
}
