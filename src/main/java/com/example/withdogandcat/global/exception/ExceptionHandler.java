package com.example.withdogandcat.global.exception;

import com.example.withdogandcat.global.common.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    /**
     * 일관된 응답 형식을 유지하기 위한 클래스
     * 애플리케이션 전반에 걸친 예외 처리
     */

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        BaseResponseStatus status = e.getStatus();
        return ResponseEntity
                .status(status.getCode())
                .body(new BaseResponse<>(status));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        BaseResponseStatus errorStatus = BaseResponseStatus.BAD_REQUEST; // 예시, 실제 상황에 맞게 조정

        return ResponseEntity
                .status(errorStatus.getCode())
                .body(new BaseResponse<>(errorStatus, "성공", errorMessage));
    }
}
