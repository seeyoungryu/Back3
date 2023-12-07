package com.example.withdogandcat.domain.pet.dto;

import com.example.withdogandcat.global.config.s3.validation.FileExtension;
import com.example.withdogandcat.global.config.s3.validation.FileSize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetRequestDto {
    private String petName;

    @FileSize(max = 1048576, message = "이미지 파일은 1MB 이하이어야 합니다")
    @FileExtension(ext = "png,jpg,jpeg", message = "이미지 파일은 png, jpg, jpeg 형식이어야 합니다")
    private MultipartFile imageUrl;

    @Builder
    public PetRequestDto(String petName, MultipartFile petImage) {
        this.petName = petName;
        this.imageUrl = petImage;
    }
}
