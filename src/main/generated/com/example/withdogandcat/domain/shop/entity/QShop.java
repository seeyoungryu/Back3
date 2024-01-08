package com.example.withdogandcat.domain.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = 929677801L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShop shop = new QShop("shop");

    public final ListPath<com.example.withdogandcat.domain.Image.Image, com.example.withdogandcat.domain.Image.QImage> images = this.<com.example.withdogandcat.domain.Image.Image, com.example.withdogandcat.domain.Image.QImage>createList("images", com.example.withdogandcat.domain.Image.Image.class, com.example.withdogandcat.domain.Image.QImage.class, PathInits.DIRECT2);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath shopAddress = createString("shopAddress");

    public final StringPath shopDescribe = createString("shopDescribe");

    public final StringPath shopEndTime = createString("shopEndTime");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath shopName = createString("shopName");

    public final StringPath shopStartTime = createString("shopStartTime");

    public final StringPath shopTel1 = createString("shopTel1");

    public final StringPath shopTel2 = createString("shopTel2");

    public final StringPath shopTel3 = createString("shopTel3");

    public final EnumPath<ShopType> shopType = createEnum("shopType", ShopType.class);

    public final com.example.withdogandcat.domain.user.entity.QUser user;

    public QShop(String variable) {
        this(Shop.class, forVariable(variable), INITS);
    }

    public QShop(Path<? extends Shop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShop(PathMetadata metadata, PathInits inits) {
        this(Shop.class, metadata, inits);
    }

    public QShop(Class<? extends Shop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.withdogandcat.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

