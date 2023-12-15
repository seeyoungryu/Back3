package com.example.withdogandcat.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private final String message;
    private final T data;

    public ApiResponseDto(String message, T data){
        this.message = message;
        this.data = data;
    }
}
