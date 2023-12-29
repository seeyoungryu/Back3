package com.example.withdogandcat.domain.like;

import com.example.mailtest.domain.review.entity.Review;
import com.example.mailtest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndReview(User user, Review review);
    void deleteByUser(User user);
    int countByReview(Review review);
}
