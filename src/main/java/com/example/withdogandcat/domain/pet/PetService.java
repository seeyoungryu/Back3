package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.tool.ApiResponseDto;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final ImageS3Service imageS3Service;

    //반려동물 등록
    @Transactional
    public PetResponseDto createPet(PetRequestDto petRequestDto ,List<MultipartFile> imageFiles,
                                      User user) throws IOException {
        Pet pet = Pet.of(petRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImages(imageFiles, pet);
        uploadedImages.forEach(pet::addImage);
        petRepository.save(pet);
        return PetResponseDto.from(pet);
    }


    //반려동물 전체 조회
    @Transactional(readOnly = true)
    public ApiResponseDto<List<PetResponseDto>> getAllPets() {
        List<PetResponseDto> pets = petRepository.findAll().stream()
                .map(PetResponseDto::from).collect(Collectors.toList());

        String message = pets.isEmpty() ? "등록된 왈왈이가 없습니다" : "왈왈이 전체 조회 성공";
        return new ApiResponseDto<>(message, pets);
    }

    //반려동물 상세조회
    @Transactional(readOnly = true)
    public PetResponseDto getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        return PetResponseDto.from(pet);
    }




    //반려동물 수정
    @Transactional
    public PetResponseDto updatePet(Long petId, PetRequestDto petRequestDto,
                                    List<MultipartFile> imageFiles, User currentUser) throws IOException {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));
        if (!pet.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        imageS3Service.deleteImages(pet.getImages());
        pet.clearImages();
        List<Image> newImages = imageS3Service.uploadMultipleImages(imageFiles, pet);
        newImages.forEach(pet::addImage);


        pet.updatePetDetails(
                petRequestDto.getPetName(),
                petRequestDto.getPetInfo(),
                petRequestDto.getPetKind(),
                petRequestDto.getPetGender());
        return PetResponseDto.from(petRepository.save(pet));
    }

    //반려동물 삭제
    @Transactional
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        imageS3Service.deleteImages(pet.getImages());
        petRepository.delete(pet);
    }

}