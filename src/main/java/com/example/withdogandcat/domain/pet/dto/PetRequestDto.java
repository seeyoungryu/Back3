package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.domain.pet.entity.PetGender;
import com.example.withdogandcat.domain.pet.entity.PetKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetRequestDto {

    @NotBlank(message = "반려동물 이름을 넣어주세요")
    @Size(min = 1, max = 10, message = "반려동물 이름은 10자 이내로 입력해주세요")
    private String petName;

    private PetGender petGender;
    private PetKind petKind;

    @Size(max =  50, message = "반려동물 정보는 50자 이내로 입력해주세요")
    private String petInfo;

    private List<MultipartFile> imageFiles;

    // @Builder
    // public PetRequestDto(String petName,
    //                      PetGender petGender,
    //                      PetKind petKind,
    //                      String petInfo,
    //                      List<MultipartFile> imageFiles) {
    //     this.petName = petName;
    //     this.petGender = petGender;
    //     this.petKind = petKind;
    //     this.petInfo = petInfo;
    //     this.imageFiles = imageFiles;
    // }
}
