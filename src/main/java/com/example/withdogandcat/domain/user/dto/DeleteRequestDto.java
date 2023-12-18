package com.example.withdogandcat.domain.user.dto;

import lombok.Getter;

@Getter
public class DeleteRequestDto {
    private String password;


    public void setPassword(String password) {
        this.password = password;
    }
}

