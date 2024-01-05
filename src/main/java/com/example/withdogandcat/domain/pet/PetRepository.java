package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);
    void deleteByUser(User user);
    int countByUser(User user);


    //정리 필요

    Page<Pet> findAll(Pageable pageable);

//    @Query("SELECT p, COUNT(pl) as petLikes FROM Pet p LEFT JOIN p.petLikes pl GROUP BY p.petId ORDER BY petLikes DESC")
//    Page<Object[]> findAllOrderByPetLikes(Pageable pageable);

    @Query("SELECT p, COUNT(pl) as petLikes FROM Pet p LEFT JOIN p.petLikes pl WHERE p.petId > :lastPetId GROUP BY p.petId ORDER BY petLikes DESC")
    List<Object[]> findPetsAfterCursor(Long lastPetId, Pageable pageable);


}

