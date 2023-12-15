package com.example.withdogandcat.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatListMessageDto {

    // 채팅방 목록과 관련된 정보 반환
    private List<ChatRoomResponseDto> chatRoomResponseDtos;

    public ChatListMessageDto(List<ChatRoomResponseDto> chatRoomResponseDtoList) {
        this.chatRoomResponseDtos = chatRoomResponseDtoList;
    }
}
