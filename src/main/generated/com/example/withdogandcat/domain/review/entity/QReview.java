package com.example.withdogandcat.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -490041747L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final QTimestamped _super = new QTimestamped(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final SetPath<com.example.withdogandcat.domain.like.Like, com.example.withdogandcat.domain.like.QLike> likes = this.<com.example.withdogandcat.domain.like.Like, com.example.withdogandcat.domain.like.QLike>createSet("likes", com.example.withdogandcat.domain.like.Like.class, com.example.withdogandcat.domain.like.QLike.class, PathInits.DIRECT2);

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final com.example.withdogandcat.domain.shop.entity.QShop shop;

    public final com.example.withdogandcat.domain.user.entity.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shop = inits.isInitialized("shop") ? new com.example.withdogandcat.domain.shop.entity.QShop(forProperty("shop"), inits.get("shop")) : null;
        this.user = inits.isInitialized("user") ? new com.example.withdogandcat.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

