package com.example.withdogandcat.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "creator_id")
    private User creatorId;

    @Builder
    private ChatRoomEntity(String roomId, String name, User creatorId) {
        this.roomId = roomId;
        this.name = name;
        this.creatorId = creatorId;
    }

}
