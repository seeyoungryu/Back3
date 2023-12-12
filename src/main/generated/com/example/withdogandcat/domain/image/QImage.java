package com.example.withdogandcat.domain.image;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImage is a Querydsl query type for Image
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImage extends EntityPathBase<Image> {

    private static final long serialVersionUID = 1377739848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImage image = new QImage("image");

    public final NumberPath<Long> imageId = createNumber("imageId", Long.class);

    public final StringPath originName = createString("originName");

    public final com.example.withdogandcat.domain.pet.entity.QPet pet;

    public final com.example.withdogandcat.domain.shop.entity.QShop shop;

    public final StringPath storedImagePath = createString("storedImagePath");

    public QImage(String variable) {
        this(Image.class, forVariable(variable), INITS);
    }

    public QImage(Path<? extends Image> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImage(PathMetadata metadata, PathInits inits) {
        this(Image.class, metadata, inits);
    }

    public QImage(Class<? extends Image> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new com.example.withdogandcat.domain.pet.entity.QPet(forProperty("pet"), inits.get("pet")) : null;
        this.shop = inits.isInitialized("shop") ? new com.example.withdogandcat.domain.shop.entity.QShop(forProperty("shop"), inits.get("shop")) : null;
    }

}

