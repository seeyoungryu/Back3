package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.pet.entity.Pet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetResponseDto {
    private Long petId;
    private String nickname;
    private String petName;
    private String petGender;
    private String petKind;
    private String petInfo;
    private String imageUrl;

    @Builder
    public PetResponseDto(Long petId, String nickname, String petName, String petGender, String petKind, String petInfo, String imageUrl) {
        this.petId = petId;
        this.nickname = nickname;
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.imageUrl = imageUrl;
    }

    public static PetResponseDto from(Pet pet) {
        return PetResponseDto.builder()
                .petId(pet.getId())
                .nickname(pet.getUser().getNickname())
                .petName(pet.getName())
                .petGender(pet.getGender().name())
                .petKind(pet.getKind().name())
                .petInfo(pet.getPetInfo())
                .imageUrl(pet.getImageUrl())
                .build();
    }
}
