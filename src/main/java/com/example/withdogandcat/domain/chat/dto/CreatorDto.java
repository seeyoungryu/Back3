package com.example.withdogandcat.domain.chat.dto;

import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorDto {

    /**
     * 채팅방 생성자 정보
     */
    private Long userId;
    private String email;
    private String nickname;
    private List<PetResponseDto> pets;

    @Builder
    public CreatorDto(Long userId, String email, String nickname, List<PetResponseDto> pets) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.pets = pets;
    }

}
