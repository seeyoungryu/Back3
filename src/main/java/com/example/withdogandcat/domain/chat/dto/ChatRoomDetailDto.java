package com.example.withdogandcat.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailDto {
    private String roomId;
    private String name;
    private String nickname;
    private List<CreatorDto> members;
}
