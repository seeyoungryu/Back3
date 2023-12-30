package com.example.withdogandcat.domain.pet.petLike;

import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetLikeService {

    private final PetLikeRepository petLikeRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public void createPetLike(Long petId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        petLikeRepository.findByUserAndPet(user, pet).ifPresent(like -> {
            throw new BaseException(BaseResponseStatus.ALREADY_LIKED);
        });

        PetLike petLike = new PetLike(user, pet);
        petLikeRepository.save(petLike);
    }

    @Transactional
    public void deletePetLike(Long petId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PET_NOT_FOUND));

        PetLike petLike = petLikeRepository.findByUserAndPet(user, pet)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LIKE_NOT_FOUND));

        petLikeRepository.delete(petLike);
    }
}
