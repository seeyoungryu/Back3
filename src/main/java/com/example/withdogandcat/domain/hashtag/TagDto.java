package com.example.withdogandcat.domain.hashtag;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {

    private Long id;
    private String name;

    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagDto from(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

}