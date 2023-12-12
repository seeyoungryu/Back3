package com.example.withdogandcat.domain.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDto> findAllRoom() {
        return chatRoomRepository.findAllRoom();
    }

    public ChatRoomDto findRoomById(String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

    public ChatRoomDto createRoom(String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    public <T> void sendMessage(WebSocketSession session, T message) throws IOException {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
