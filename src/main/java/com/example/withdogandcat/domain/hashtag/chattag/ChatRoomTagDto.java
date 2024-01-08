package com.example.withdogandcat.domain.hashtag.chattag;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomTagDto implements TagDtoInterface {

    private Long id;

    @Size(max = 7, message = "해시태그는 최대 7자까지 가능합니다.")
    private String name;

    public ChatRoomTagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChatRoomTagDto from(ChatRoomTag chatRoomTag) {
        return new ChatRoomTagDto(chatRoomTag.getId(), chatRoomTag.getName());
    }

}
