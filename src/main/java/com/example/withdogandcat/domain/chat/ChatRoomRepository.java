package com.example.withdogandcat.domain.chat;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {
    private final Map<String, ChatRoomDto> chatRoomMap = new LinkedHashMap<>();

    public List<ChatRoomDto> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRoomDtos);
        return chatRoomDtos;
    }

    public ChatRoomDto findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    public ChatRoomDto createChatRoom(String name) {
        ChatRoomDto chatRoomDto = ChatRoomDto.create(name);
        chatRoomMap.put(chatRoomDto.getRoomId(), chatRoomDto);
        return chatRoomDto;
    }
}
