package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "chatroom_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomTag extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public ChatRoomTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChatRoomTag from(ChatRoomTagDto chatRoomTagDto) {
        return ChatRoomTag.builder()
                .id(chatRoomTagDto.getId())
                .name(chatRoomTagDto.getName())
                .build();
    }
}
