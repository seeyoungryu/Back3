package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.domain.hashtag.TagDtoInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomTagDto implements TagDtoInterface {

    private Long id;
    private String name;

    public ChatRoomTagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChatRoomTagDto from(ChatRoomTag chatRoomTag) {
        return new ChatRoomTagDto(chatRoomTag.getId(), chatRoomTag.getName());
    }
}
