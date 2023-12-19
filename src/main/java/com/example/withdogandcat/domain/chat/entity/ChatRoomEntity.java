package com.example.withdogandcat.domain.chat.entity;

import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomId; // 고유한 방 ID

    @Column(nullable = false)
    private String name; // 채팅방 이름

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creatorId; // 채팅방 생성자

}
