package com.example.withdogandcat.domain.hashtag.shoptag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShopTag is a Querydsl query type for ShopTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopTag extends EntityPathBase<ShopTag> {

    private static final long serialVersionUID = -1927252776L;

    public static final QShopTag shopTag = new QShopTag("shopTag");

    public final com.example.withdogandcat.global.common.QTimestamped _super = new com.example.withdogandcat.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QShopTag(String variable) {
        super(ShopTag.class, forVariable(variable));
    }

    public QShopTag(Path<? extends ShopTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShopTag(PathMetadata metadata) {
        super(ShopTag.class, metadata);
    }

}

