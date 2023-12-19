package com.example.withdogandcat.global.common;

import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    /**
     * 모든 API 응답을 나타내는 범용 클래스
     */

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 일반 응답
     */
    public BaseResponse(BaseResponseStatus status, String 로그인_성공, T result){
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.result = result;
    }

    /**
     * 오류 응답
     */
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.result = null; // 오류 응답에서는 결과가 없음
    }

}
