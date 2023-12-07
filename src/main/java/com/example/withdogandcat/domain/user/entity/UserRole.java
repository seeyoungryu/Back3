package com.example.withdogandcat.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER(Authority.USER);

    private final String authority;

    public static class Authority {
        public static final String USER = "ROLE_USER";
    }
}
