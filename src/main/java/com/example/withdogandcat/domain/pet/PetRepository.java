package com.example.withdogandcat.domain.pet;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);
    void deleteByUser(User user);
    int countByUser(User user);
}