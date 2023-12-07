package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByName(String name);
}