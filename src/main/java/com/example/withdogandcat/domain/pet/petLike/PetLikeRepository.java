package com.example.withdogandcat.domain.pet.petLike;

import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    Optional<PetLike> findByUserAndPet(User user, Pet pet);

    void deleteByUser(User user);

    Long countByPet(Pet pet);

    // Pet 엔티티와 연관된 모든 PetLike 엔티티를 삭제하는 메서드
    void deleteByPet(Pet pet);
}