package com.example.withdogandcat.domain.review.repository;

import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.shop.entitiy.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByShop(Shop shop);

    Optional<Review> findByReviewIdAndShop(Long reviewId, Shop shop);
}
