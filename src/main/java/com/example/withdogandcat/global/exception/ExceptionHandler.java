package com.example.withdogandcat.global.exception;

import com.example.withdogandcat.global.common.BaseResponse;
import org.springframework.http.HttpStatus;
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
        HttpStatus httpStatus = HttpStatus.resolve(status.getCode());
        if (httpStatus == null) {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity
                .status(httpStatus)
                .body(new BaseResponse<>(false, status.getCode(), status.getMessage(), null));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), errorMessage, null));
    }

}
