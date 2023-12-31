package com.example.withdogandcat.domain.chat.dto;

import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.hashtag.chattag.ChatRoomTagDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<ChatRoomTagDto> tags;


    @Builder
    public ChatRoomListDto(String roomId, String name, CreatorDto creator, ChatMessage lastTalkMessage, List<ChatRoomTagDto> tags) {
        this.roomId = roomId;
        this.name = name;
        this.creator = creator;
        this.lastTalkMessage = lastTalkMessage;
        this.tags = tags;
    }

}
