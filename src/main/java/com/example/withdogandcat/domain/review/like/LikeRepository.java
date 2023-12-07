package com.example.withdogandcat.domain.review.like;

import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndReview(User user, Review review);

    int countByReview(Review review);
}
