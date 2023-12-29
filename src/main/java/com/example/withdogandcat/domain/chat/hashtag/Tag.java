package com.example.withdogandcat.domain.chat.hashtag;

import com.example.mailtest.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    public static Tag from(TagDto tagDto) {
        return Tag.builder()
                .name(tagDto.getName())
                .build();
    }

}
