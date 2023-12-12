package com.example.withdogandcat.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private String roomId;
    private String name;

    public static ChatRoomDto create(String name) {
        return ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .name(name)
                .build();
    }
}
