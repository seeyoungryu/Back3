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

        // '좋아요' 수가 0으로 시작하므로 새로운 오버로딩된 메소드 사용
        PetResponseDto responseDto = PetResponseDto.from(pet); // 오버로딩된 메소드 사용

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }




    //petresponsedto 수정 이전 코드
//    @Transactional
//    public BaseResponse<PetResponseDto> createPet(PetRequestDto petRequestDto, List<MultipartFile> imageFiles, User user) throws IOException {
//        int currentPetCount = petRepository.countByUser(user);
//        if (currentPetCount >= MAX_PETS_PER_USER) {
//            throw new BaseException(BaseResponseStatus.EXCEED_MAX_PET_LIMIT);
//        }
//
//        Pet pet = Pet.of(petRequestDto, user);
//        List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForPet(imageFiles, pet);
//        uploadedImages.forEach(pet::addImage);
//        petRepository.save(pet);
//
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", PetResponseDto.from(pet, 0L));
//    }




//    //페이지네이션 (프론트 요구사항:nextpage 반영)
//
//    @Transactional(readOnly = true)
//    public BaseResponse<Map<String, Object>> getAllPetsSortedByPetLikes(Pageable pageable) {
//        Page<PetResponseDto> petsPage = petRepository.findAllOrderByPetLikes(pageable)
//                .map(objects -> {
//                    Pet pet = (Pet) objects[0];
//                    Long petLikes = (Long) objects[1];
//                    return PetResponseDto.from(pet, petLikes);
//                });
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("data", petsPage.getContent());
//        response.put("nextPage", petsPage.hasNext() ? petsPage.getNumber() + 2 : null); // 페이지 인덱스는 0부터 시작하므로 +2를 합니다.
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", response);
//    }


    @Transactional(readOnly = true)
    public BaseResponse<Map<String, Object>> getAllPetsSortedByPetLikes(Long lastPetId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "petLikes"));

        List<PetResponseDto> pets;
        if (lastPetId == null) {
            // lastPetId가 null인 경우, 최초 요청으로 처리
            pets = petRepository.findAll(pageable).stream()
                    .map(pet -> PetResponseDto.from(pet, petLikeRepository.countByPet(pet)))
                    .collect(Collectors.toList());
        } else {
            // lastPetId가 있는 경우, 커서 기반 로직 적용
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

        // pet_likes 테이블에서 해당 Pet과 관련된 레코드를 먼저 삭제
        petLikeRepository.deleteByPet(pet);

        // Pet 엔티티와 관련된 이미지를 삭제
        imageS3Service.deleteImages(pet.getImages());
        petRepository.delete(pet);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }



    @Transactional
    public BaseResponse<List<PetResponseDto>> getAllPetsWithoutLikes() {
        List<Pet> pets = petRepository.findAll();
        List<PetResponseDto> petResponseDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", petResponseDtos);
    }


}
