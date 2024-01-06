package com.example.withdogandcat.domain.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPet is a Querydsl query type for Pet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPet extends EntityPathBase<Pet> {

    private static final long serialVersionUID = 1370664771L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPet pet = new QPet("pet");

    public final ListPath<com.example.withdogandcat.domain.Image.Image, com.example.withdogandcat.domain.Image.QImage> images = this.<com.example.withdogandcat.domain.Image.Image, com.example.withdogandcat.domain.Image.QImage>createList("images", com.example.withdogandcat.domain.Image.Image.class, com.example.withdogandcat.domain.Image.QImage.class, PathInits.DIRECT2);

    public final EnumPath<PetGender> petGender = createEnum("petGender", PetGender.class);

    public final NumberPath<Long> petId = createNumber("petId", Long.class);

    public final StringPath petInfo = createString("petInfo");

    public final EnumPath<PetKind> petKind = createEnum("petKind", PetKind.class);

    public final StringPath petName = createString("petName");

    public final com.example.withdogandcat.domain.user.entity.QUser user;

    public QPet(String variable) {
        this(Pet.class, forVariable(variable), INITS);
    }

    public QPet(Path<? extends Pet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPet(PathMetadata metadata, PathInits inits) {
        this(Pet.class, metadata, inits);
    }

    public QPet(Class<? extends Pet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.withdogandcat.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

