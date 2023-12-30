package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.Image.Image;
import com.example.withdogandcat.domain.Image.ImageS3Service;
import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.pet.petLike.PetLikeRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private static final int MAX_PETS_PER_USER = 10;
    private final PetRepository petRepository;
    private final ImageS3Service imageS3Service;
    private final PetLikeRepository petLikeRepository;

    //펫 생성, 새로 생성된 Pet 객체의 '좋아요' 수 -> 0 설정
    @Transactional
    public BaseResponse<PetResponseDto> createPet(PetRequestDto petRequestDto, List<MultipartFile> imageFiles, User user) throws IOException {
        int currentPetCount = petRepository.countByUser(user);
        if (currentPetCount >= MAX_PETS_PER_USER) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_PET_LIMIT);
        }

        Pet pet = Pet.of(petRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPet(imageFiles, pet);
        uploadedImages.forEach(pet::addImage);
        petRepository.save(pet);


        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", PetResponseDto.from(pet, 0L));
    }


    // 좋아요 순으로 정렬된 전체 펫 조회 (페이지네이션)
    @Transactional(readOnly = true)
    public BaseResponse<Page<PetResponseDto>> getAllPetsSortedByPetLikes(Pageable pageable) {
        Page<PetResponseDto> pets = petRepository.findAllOrderByPetLikes(pageable)
                .map(objects -> {
                    Pet pet = (Pet) objects[0];
                    Long petLikes = (Long) objects[1];
                    return PetResponseDto.from(pet, petLikes);
                });
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets);
    }


    // 상세조회 (펫 단건)
    @Transactional(readOnly = true)
    public BaseResponse<PetResponseDto> getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        Long petLikes = petLikeRepository.countByPet(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", PetResponseDto.from(pet, petLikes));
    }


    @Transactional
    public BaseResponse<PetResponseDto> updatePet(Long petId, PetRequestDto petRequestDto,
                                                  List<MultipartFile> imageFiles, User currentUser) throws IOException {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        if (!pet.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }

        if (imageFiles != null && !imageFiles.isEmpty() && !imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            imageS3Service.deleteImages(pet.getImages());
            pet.clearImages();
            List<Image> newImages = imageS3Service.uploadMultipleImagesForPet(imageFiles, pet);
            newImages.forEach(pet::addImage);
        }

        pet.updatePetDetails(
                petRequestDto.getPetName(),
                petRequestDto.getPetInfo(),
                petRequestDto.getPetKind(),
                petRequestDto.getPetGender());
        Pet updatedPet = petRepository.save(pet);

        // '좋아요' 수 조회 및 Long 타입으로 변환
        Long petLikes = Long.valueOf(petLikeRepository.countByPet(updatedPet));

        // '좋아요' 수를 포함하여 DTO 생성
        PetResponseDto responseDto = PetResponseDto.from(updatedPet, petLikes);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }


    @Transactional
    public BaseResponse<Void> deletePet(Long petId, User currentUser) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        if (!pet.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        imageS3Service.deleteImages(pet.getImages());
        petRepository.delete(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }

}
