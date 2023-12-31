package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chat_room_tag_map")
@NoArgsConstructor
public class ChatRoomTagMap extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_tag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_tag_id")
    private ChatRoomTag chatRoomTag;

    @Builder
    public ChatRoomTagMap(ChatRoomEntity chatRoom, ChatRoomTag chatRoomTag) {
        this.chatRoom = chatRoom;
        this.chatRoomTag = chatRoomTag;
    }

}
