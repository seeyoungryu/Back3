package com.example.withdogandcat.domain.chat.hashtag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {

    private Long id;
    private String name;

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagDto from(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
