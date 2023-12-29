package com.example.withdogandcat.domain.chat.dto;

import com.example.mailtest.domain.pet.dto.PetResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    /**
     * 채팅방 사용자 정보
     * 펫관련 정보 포함
     */

    private Long userId;
    private String email;
    private String nickname;
    private List<PetResponseDto> pets;

}
