package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 애완동물 엔티티에 대한 접근을 제공하는 JPA 리포지토리 인터페이스
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByName(String name); // 이름으로 중복 검사를 위한 메소드
}