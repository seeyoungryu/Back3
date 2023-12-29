package com.example.withdogandcat.domain.chat.entity;

import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id")  // 이 부분을 확인하고 수정해야 합니다.
    private User creatorId;  // User 엔티티와의 관계 설정을 올바르게 수정합니다.

    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL)  // 채팅방과 채팅 메시지 간의 관계 설정
    private List<ChatMessageEntity> messages = new ArrayList<>();

    @Builder
    private ChatRoomEntity(String roomId, String name, User creatorId) {
        this.roomId = roomId;
        this.name = name;
        this.creatorId = creatorId;
    }

}
