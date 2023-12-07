package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.pet.entity.Pet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetResponseDto {
    private Long id;
    private Long petId;
    private String petName;
    private String imageUrl;

    @Builder
    public PetResponseDto(Long id, Long petId, String petName, String imageUrl) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.imageUrl = imageUrl;
    }

    public static PetResponseDto from(Pet pet) {
        return PetResponseDto.builder()
                .id(1L)
                .petId(pet.getId())
                .petName(pet.getName())
                .imageUrl(pet.getImageUrl())
                .build();
    }
}