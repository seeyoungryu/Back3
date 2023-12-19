package com.example.withdogandcat.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {
    private String roomId;
    private String name;
    private String nickname;
}
