package com.example.withdogandcat.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

        @Query("SELECT i FROM Image i WHERE i.pet.user.userId = :userId OR i.shop.user.userId = :userId")
        List<Image> findByUserId(@Param("userId") Long userId);
}
