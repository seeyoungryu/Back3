package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.pet.entity.Pet;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PetResponseDto {
    private final Long petId;
    private final String nickname;
    private final String petName;
    private final String petGender;
    private final String petKind;
    private final String petInfo;
    private final List<String> imageUrls;

    @Builder
    public PetResponseDto(Long petId, String nickname, String petName,
                          String petGender, String petKind,
                          String petInfo, List<String> imageUrls) {
        this.petId = petId;
        this.nickname = nickname;
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.imageUrls = imageUrls;
    }

    public static PetResponseDto from(Pet pet) {

        List<String> imageUrls = pet.getImages().stream()
                .map(image -> image.getStoredImagePath())
                .collect(Collectors.toList());

        return PetResponseDto.builder()
                .petId(pet.getPetId())
                .nickname(pet.getUser().getNickname())
                .petName(pet.getPetName())
                .petGender(pet.getPetGender().name())
                .petKind(pet.getPetKind().name())
                .petInfo(pet.getPetInfo())
                .imageUrls(imageUrls)
                .build();
    }
}
