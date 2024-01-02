package com.example.withdogandcat.domain.hashtag.shoptag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopTagMap is a Querydsl query type for ShopTagMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopTagMap extends EntityPathBase<ShopTagMap> {

    private static final long serialVersionUID = 335440228L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopTagMap shopTagMap = new QShopTagMap("shopTagMap");

    public final com.example.withdogandcat.global.common.QTimestamped _super = new com.example.withdogandcat.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.withdogandcat.domain.shop.entity.QShop shop;

    public final QShopTag shopTag;

    public QShopTagMap(String variable) {
        this(ShopTagMap.class, forVariable(variable), INITS);
    }

    public QShopTagMap(Path<? extends ShopTagMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopTagMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopTagMap(PathMetadata metadata, PathInits inits) {
        this(ShopTagMap.class, metadata, inits);
    }

    public QShopTagMap(Class<? extends ShopTagMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shop = inits.isInitialized("shop") ? new com.example.withdogandcat.domain.shop.entity.QShop(forProperty("shop"), inits.get("shop")) : null;
        this.shopTag = inits.isInitialized("shopTag") ? new QShopTag(forProperty("shopTag")) : null;
    }

}

