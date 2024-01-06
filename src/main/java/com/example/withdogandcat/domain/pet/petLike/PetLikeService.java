//package com.example.withdogandcat.domain.pet.petLike;
//
//import com.example.withdogandcat.domain.pet.PetRepository;
//import com.example.withdogandcat.domain.pet.entity.Pet;
//import com.example.withdogandcat.domain.review.entity.Review;
//import com.example.withdogandcat.domain.user.UserRepository;
//import com.example.withdogandcat.domain.user.entity.User;
//import com.example.withdogandcat.global.exception.BaseException;
//import com.example.withdogandcat.global.exception.BaseResponseStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class PetLikeService {
//
//    private final PetRepository petRepository;
//    private final UserRepository userRepository;
//    private final PetLikeRepository petLikeRepository;
//
//
//
//    /**
//     * 반려동물 좋아요 등록
//     */
//    @Transactional
//    public void createPetLike(Long petId, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
//        Pet pet = petRepository.findById(petId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));
//
//        petLikeRepository.findByUserAndPet(user, pet).ifPresent(like -> {
//            throw new BaseException(BaseResponseStatus.ALREADY_LIKED);
//        });
//
//        PetLike petLike = new PetLike(user, pet);
//        petLikeRepository.save(petLike);
//    }
//
//
//
//    /**
//     * 반려동물 좋아요 등록취소(삭제)
//     */
//    @Transactional
//    public void deletePetLike(Long petId, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
//        Pet pet = petRepository.findById(petId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));
//
//        PetLike petLike = petLikeRepository.findByUserAndPet(user, pet)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.LIKE_NOT_FOUND));
//
//        petLikeRepository.delete(petLike);
//    }
//
//
//
//    /**
//     * 사용자가 특정 반려동물에 좋아요를 등록했는지 확인
//     */
//    @Transactional(readOnly = true)
//    public boolean isPetLiked(Long userId, Long petId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
//        Pet pet = petRepository.findById(petId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));
//
//        return petLikeRepository.findByUserAndPet(user, pet).isPresent();
//    }
//}
