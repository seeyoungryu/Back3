package com.example.withdogandcat.domain.chat.dto;

import com.example.mailtest.domain.chat.entity.ChatMessage;
import com.example.mailtest.domain.chat.hashtag.TagDto;
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
    private List<TagDto> tags;


    @Builder
    public ChatRoomListDto(String roomId, String name, CreatorDto creator, ChatMessage lastTalkMessage, List<TagDto> tags) {
        this.roomId = roomId;
        this.name = name;
        this.creator = creator;
        this.lastTalkMessage = lastTalkMessage;
        this.tags = tags;
    }

}
