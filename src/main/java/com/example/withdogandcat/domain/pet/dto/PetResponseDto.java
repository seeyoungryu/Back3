package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.Image.Image;
import com.example.withdogandcat.domain.pet.entity.Pet;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PetResponseDto {

    private final Long userId;
    private final Long petId;
    private final String nickname;
    private final String petName;
    private final String petGender;
    private final String petKind;
    private final String petInfo;
    private final List<String> imageUrls;
    private final Long petLikes;

    @Builder
    public PetResponseDto(Long userId, Long petId, String nickname, String petName,
                          String petGender, String petKind,
                          String petInfo, List<String> imageUrls, Long petLikes) {
        this.userId = userId;
        this.petId = petId;
        this.nickname = nickname;
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.imageUrls = imageUrls;
        this.petLikes = petLikes;
    }

    public static PetResponseDto from(Pet pet, Long petLikes) {
        List<String> imageUrls = pet.getImages().stream()
                .map(Image::getStoredImagePath)
                .collect(Collectors.toList());

        return PetResponseDto.builder()
                .petId(pet.getPetId())
                .userId(pet.getUser().getUserId())
                .nickname(pet.getUser().getNickname())
                .petName(pet.getPetName())
                .petGender(pet.getPetGender().name())
                .petKind(pet.getPetKind().name())
                .petInfo(pet.getPetInfo())
                .imageUrls(imageUrls)
                .petLikes(petLikes)
                .build();
    }
}
