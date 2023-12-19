package com.example.withdogandcat.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    /**
     * 특정한 오류 상황을 나타내기 위해 사용자 정의 예외를 만들 때 활용
     */

    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

}
