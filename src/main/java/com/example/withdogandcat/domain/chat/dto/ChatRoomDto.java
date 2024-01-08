package com.example.withdogandcat.domain.chat.dto;

import com.example.withdogandcat.domain.hashtag.chattag.ChatRoomTagDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDto {

    private Long id;
    private String roomId;
    private String name;
    private LocalDateTime createdAt;
    private CreatorDto creator;
    private List<ChatRoomTagDto> tags;

    @Builder
    public ChatRoomDto(Long id, String roomId, String name, LocalDateTime createdAt, CreatorDto creator, List<ChatRoomTagDto> tags) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.createdAt = createdAt;
        this.creator = creator;
        this.tags = tags;
    }

}
