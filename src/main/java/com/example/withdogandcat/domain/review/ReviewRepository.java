package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByShop(Shop shop);

    @Query("SELECT r FROM Review r WHERE r.shop.shopId = :shopId")
    List<Review> findByShopId(@Param("shopId") Long shopId);

    Optional<Review> findByReviewIdAndShop(Long reviewId, Shop shop);

    boolean existsByUserAndShop(User user, Shop shop);

    void deleteByShop(Shop shop);

    void deleteByUser(User user);
}
