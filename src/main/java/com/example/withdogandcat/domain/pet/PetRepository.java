package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);
    void deleteByUser(User user);
    int countByUser(User user);
}