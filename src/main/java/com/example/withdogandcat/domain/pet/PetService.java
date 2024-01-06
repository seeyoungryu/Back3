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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private static final int MAX_PETS_PER_USER = 10;
    private final PetRepository petRepository;
    private final ImageS3Service imageS3Service;
    private final PetLikeRepository petLikeRepository;



    /**
     * 반려동물 등록
     */
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

        PetResponseDto responseDto = PetResponseDto.from(pet);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }



    /**
     * 등록 반려동물 전체 조회(좋아요 및 무한스크롤 적용)
     */
    @Transactional(readOnly = true)
    public BaseResponse<Map<String, Object>> getAllPetsSortedByPetLikes(Long lastPetId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "petLikes"));

        List<PetResponseDto> pets;
        if (lastPetId == null) {
            pets = petRepository.findAll(pageable).stream()
                    .map(pet -> PetResponseDto.from(pet, petLikeRepository.countByPet(pet)))
                    .collect(Collectors.toList());
        } else {
            pets = petRepository.findPetsAfterCursor(lastPetId, pageable).stream()
                    .map(objects -> {
                        Pet pet = (Pet) objects[0];
                        Long petLikes = (Long) objects[1];
                        return PetResponseDto.from(pet, petLikes);
                    }).collect(Collectors.toList());
        }

        String nextCursor = pets.isEmpty() ? null : pets.get(pets.size() - 1).getPetId().toString();

        Map<String, Object> response = new HashMap<>();
        response.put("data", pets);
        response.put("nextCursor", nextCursor);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", response);
    }



    /**
     * 등록 반려동물 상세조회 (단건)
     */
    @Transactional(readOnly = true)
    public BaseResponse<PetResponseDto> getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        Long petLikes = petLikeRepository.countByPet(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", PetResponseDto.from(pet, petLikes));
    }



    /**
     * 등록 반려동물 수정
     */
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

        Long petLikes = Long.valueOf(petLikeRepository.countByPet(updatedPet));

        PetResponseDto responseDto = PetResponseDto.from(updatedPet, petLikes);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }



    /**
     * 등록 반려동물 삭제
     */
    @Transactional
    public BaseResponse<Void> deletePet(Long petId, User currentUser) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        if (!pet.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        petLikeRepository.deleteByPet(pet);

        imageS3Service.deleteImages(pet.getImages());
        petRepository.delete(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }



    /**
     * 좋아요 제외 단순 전체 조회
     */
    @Transactional
    public BaseResponse<List<PetResponseDto>> getAllPetsWithoutLikes() {
        List<Pet> pets = petRepository.findAll();
        List<PetResponseDto> petResponseDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", petResponseDtos);
    }


}
