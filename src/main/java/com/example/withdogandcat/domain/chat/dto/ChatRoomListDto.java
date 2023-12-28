package com.example.withdogandcat.domain.chat.dto;

import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomListDto {

    /**
     * 채팅방 전체 조회시 반환 값
     */
    private String roomId;
    private String name;
    private CreatorDto creator;
    private ChatMessage lastTalkMessage;

    @Builder
    public ChatRoomListDto(String roomId, String name, CreatorDto creator, ChatMessage lastTalkMessage) {
        this.roomId = roomId;
        this.name = name;
        this.creator = creator;
        this.lastTalkMessage = lastTalkMessage;
    }

}
