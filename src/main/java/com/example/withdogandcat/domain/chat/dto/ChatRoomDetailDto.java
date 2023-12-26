package com.example.withdogandcat.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailDto {

    /**
     * 특정 채팅방 상세 조회시 반환 값
     * 채팅방에 입장해있는 유저 목록까지 반환
     */
    private String roomId;
    private String name;
    private CreatorDto creator;
    private List<UserInfoDto> members;

    @Setter
    private long userCount; // 현재 채팅방의 사용자 수

}
