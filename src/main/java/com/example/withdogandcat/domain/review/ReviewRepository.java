package com.example.withdogandcat.domain.review;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByShop(Shop shop);

    @Query("SELECT r FROM Review r WHERE r.shop.shopId = :shopId")
    List<Review> findByShopId(@Param("shopId") Long shopId);

    Optional<Review> findByReviewIdAndShop(Long reviewId, Shop shop);

    void deleteByShop(Shop shop);

    void deleteByUser(User user);
}
