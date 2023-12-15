package com.example.withdogandcat.domain.chat.model;

import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private long userCount; // 채팅방 인원수

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator; // 채팅방 생성자

    public static ChatRoom create(String name, User creator) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.creator = creator;
        return chatRoom;
    }
}
