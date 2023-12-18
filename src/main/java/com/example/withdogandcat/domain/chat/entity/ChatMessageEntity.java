package com.example.withdogandcat.domain.chat.entity;


import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입

    private String roomId; // 방 번호

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender; // 메시지 보낸사람, User 엔티티 참조

    private String senderNickname; // 메시지 보낸사람의 닉네임

    private String message; // 메시지

}
