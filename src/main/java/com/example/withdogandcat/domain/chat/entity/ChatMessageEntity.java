package com.example.withdogandcat.domain.chat.entity;

import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String roomId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User sender;

    private String message;

    public void setType(MessageType type) {
        this.type = type;
    }

    @Builder
    private ChatMessageEntity(MessageType type, String roomId, User sender, String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

}
