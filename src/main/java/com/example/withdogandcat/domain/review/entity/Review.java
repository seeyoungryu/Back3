package com.example.withdogandcat.domain.review.entity;

import com.example.withdogandcat.domain.review.like.Like;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private Set<Like> likes;

    @Builder
    private Review(User user, String comment, Shop shop) {
        this.user = user;
        this.comment = comment;
        this.shop = shop;
    }

    public static Review createReview(User user, String commentContent, Shop shop) {
        return Review.builder()
                .user(user)
                .comment(commentContent)
                .shop(shop)
                .build();
    }
}
