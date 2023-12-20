package com.example.withdogandcat.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {

    /**
     * 채팅방 전체 조회시 반환 값
     */
    private String roomId;
    private String name;
    private String nickname;

}
