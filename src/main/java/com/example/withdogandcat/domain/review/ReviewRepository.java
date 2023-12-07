package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByShop(Shop shop);

    Optional<Review> findByReviewIdAndShop(Long reviewId, Shop shop);
}
