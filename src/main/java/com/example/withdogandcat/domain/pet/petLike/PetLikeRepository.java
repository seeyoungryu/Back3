package com.example.withdogandcat.domain.pet.petLike;

import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    Optional<PetLike> findByUserAndPet(User user, Pet pet);

    //2차 정리예정, 확인필요
    void deleteByUser(User user);

    Long countByPet(Pet pet);

    void deleteByPet(Pet pet);
}