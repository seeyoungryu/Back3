package com.example.withdogandcat.domain.chat.hashtag;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chat_room_tag_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomTagMap extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_tag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public ChatRoomTagMap(ChatRoomEntity chatRoom, Tag tag) {
        this.chatRoom = chatRoom;
        this.tag = tag;
    }

}
