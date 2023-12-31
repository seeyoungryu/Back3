package com.example.withdogandcat.domain.chat.util;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.chat.dto.CreatorDto;
import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.hashtag.chattag.ChatRoomTagDto;
import com.example.withdogandcat.domain.user.entity.User;

import java.util.List;

public class ChatRoomMapper {

    /**
     * 중복메서드를 분리하기 위한 클래스
     */

    public static ChatRoomDto toDto(ChatRoomEntity chatRoomEntity) {
        return ChatRoomDto.builder()
                .id(chatRoomEntity.getId())
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .createdAt(chatRoomEntity.getCreatedAt())
                .creator(toCreatorDto(chatRoomEntity.getCreatorId()))
                .build();
    }

    public static ChatRoomListDto toChatRoomListDto(ChatRoomEntity chatRoomEntity,
                                                    ChatMessage lastTalkMessage,
                                                    List<ChatRoomTagDto> tags) {
        return ChatRoomListDto.builder()
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .creator(toCreatorDto(chatRoomEntity.getCreatorId()))
                .lastTalkMessage(lastTalkMessage)
                .tags(tags)
                .build();
    }

    public static CreatorDto toCreatorDto(User user) {
        return CreatorDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
