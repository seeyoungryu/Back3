package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.pet.entity.PetGender;
import com.example.withdogandcat.domain.pet.entity.PetKind;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetRequestDto {
    private String petName;
    private PetGender petGender;
    private PetKind petKind;
    private String petInfo;

    private List<MultipartFile> imageFiles;

    @Builder
    public PetRequestDto(String petName,
                         PetGender petGender,
                         PetKind petKind,
                         String petInfo,
                         List<MultipartFile> imageFiles) {
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.imageFiles = imageFiles;
    }
}
