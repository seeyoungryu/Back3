package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
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

    // 마이페이지 반려동물 조회
    @Transactional(readOnly = true)
    public BaseResponse<List<PetResponseDto>> getUserPets(User currentUser) {
        List<Pet> pets = petRepository.findByUser(currentUser);
        List<PetResponseDto> petDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", petDtos);
    }

    // 반려동물 등록
    @Transactional
    public BaseResponse<PetResponseDto> createPet(PetRequestDto petRequestDto, List<MultipartFile> imageFiles,
                                                  User user) throws IOException {
        Pet pet = Pet.of(petRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImages(imageFiles, pet);
        uploadedImages.forEach(pet::addImage);
        petRepository.save(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", PetResponseDto.from(pet));
    }

    // 반려동물 전체 조회
    @Transactional(readOnly = true)
    public BaseResponse<List<PetResponseDto>> getAllPets() {
        List<PetResponseDto> pets = petRepository.findAll().stream()
                .map(PetResponseDto::from).collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", pets);
    }

    // 반려동물 상세조회
    @Transactional(readOnly = true)
    public BaseResponse<PetResponseDto> getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", PetResponseDto.from(pet));
    }

    // 반려동물 수정
    @Transactional
    public BaseResponse<PetResponseDto> updatePet(Long petId, PetRequestDto petRequestDto,
                                                  List<MultipartFile> imageFiles, User currentUser) throws IOException {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));
        if (!pet.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
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
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", PetResponseDto.from(petRepository.save(pet)));
    }

    // 반려동물 삭제
    @Transactional
    public BaseResponse<Void> deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        imageS3Service.deleteImages(pet.getImages());
        petRepository.delete(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", null);
    }

}
