package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.global.config.s3.S3Upload; // 변경된 import
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException; // 추가된 import

@Service
@RequiredArgsConstructor
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final S3Upload s3Upload;

    private static final String PETINFO_BUCKET = "petinfo-pet";

    @Transactional
    public PetResponseDto registerPet(Long userId, PetRequestDto requestDto) throws IOException {
        validateDuplicatePet(requestDto.getPetName());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String imageUrl = null;
        if (requestDto.getImageUrl() != null && !requestDto.getImageUrl().isEmpty()) {
            imageUrl = s3Upload.upload(requestDto.getImageUrl(), PETINFO_BUCKET);
        }
        Pet pet = Pet.createPet(requestDto, user, imageUrl, requestDto.getImageUrl() != null ? requestDto.getImageUrl().getOriginalFilename() : null);
        pet = petRepository.save(pet);
        return PetResponseDto.from(pet);
    }


    @Transactional(readOnly = true)
    public PetResponseDto getPet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        return PetResponseDto.from(pet);
    }


    @Transactional
    public PetResponseDto updatePet(Long petId, PetRequestDto requestDto) throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        if (requestDto.getImageUrl() != null && !requestDto.getImageUrl().isEmpty()) {
            String imageUrl = s3Upload.upload(requestDto.getImageUrl(), PETINFO_BUCKET);
            pet.updateImage(imageUrl, requestDto.getImageUrl().getOriginalFilename());
        }

        if (requestDto.getPetName() != null && !requestDto.getPetName().isEmpty()) {
            validateDuplicatePet(requestDto.getPetName());
            pet.updateName(requestDto.getPetName());
        }

        return PetResponseDto.from(pet);
    }

    private void validateDuplicatePet(String name) {
        if (petRepository.existsByName(name)) {
            throw new CustomException(ErrorCode.PET_ALREADY_EXISTS);
        }
    }
}