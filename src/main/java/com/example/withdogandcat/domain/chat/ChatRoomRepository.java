package com.example.withdogandcat.domain.chat;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {

    // 채팅관련 클래스들은 jpa에서 영속성데이터로 관리하지 않기때문에 Entity, Jparepository 필요 X

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
