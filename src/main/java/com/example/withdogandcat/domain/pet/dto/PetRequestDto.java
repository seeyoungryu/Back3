package com.example.withdogandcat.domain.pet.dto;
import com.example.withdogandcat.domain.pet.entity.PetGender;
import com.example.withdogandcat.domain.pet.entity.PetKind;
import com.example.withdogandcat.global.config.s3.validation.FileExtension;
import com.example.withdogandcat.global.config.s3.validation.FileSize;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetRequestDto {
    private String petName;
    private PetGender petGender; // Enum
    private PetKind petKind; // Enum
    private String petInfo;

    @FileSize(max = 1048576, message = "이미지 파일은 1MB 이하이어야 합니다")
    @FileExtension(ext = "png,jpg,jpeg", message = "이미지 파일은 png, jpg, jpeg 형식이어야 합니다")
    private MultipartFile imageUrl;

    @Builder
    public PetRequestDto(String petName,
                         PetGender petGender,
                         PetKind petKind,
                         String petInfo,
                         MultipartFile imageUrl) {
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.imageUrl = imageUrl;
    }
}
