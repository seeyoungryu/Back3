package com.example.withdogandcat.domain.chat.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDto {
    private Long id;
    private String roomId;
    private String name;
    private LocalDateTime createdAt;
    private CreatorDto creatorId;

    @Builder
    public ChatRoomDto(Long id, String roomId, String name, LocalDateTime createdAt, CreatorDto creatorId) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.createdAt = createdAt;
        this.creatorId = creatorId;
    }
}
