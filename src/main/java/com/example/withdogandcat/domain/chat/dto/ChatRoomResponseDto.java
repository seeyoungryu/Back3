package com.example.withdogandcat.domain.chat.dto;

import com.example.withdogandcat.domain.chat.model.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private String roomId;
    private String name;
    private long userCount;


    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        ChatRoomResponseDto dto = new ChatRoomResponseDto();
        dto.roomId = chatRoom.getRoomId();
        dto.name = chatRoom.getName();
        dto.userCount = chatRoom.getUserCount();
        return dto;
    }
}
