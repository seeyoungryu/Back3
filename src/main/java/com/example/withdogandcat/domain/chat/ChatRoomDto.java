package com.example.withdogandcat.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ChatRoomDto {

    // 채팅관련 클래스들은 jpa에서 영속성데이터로 관리하지 않기때문에 Entity, Jparepository 필요 X

    private String roomId;
    private String name;

    public static ChatRoomDto create(String name) {
        return ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .name(name)
                .build();
    }
}
