package com.example.withdogandcat.domain.pet.petLike;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetLike is a Querydsl query type for PetLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetLike extends EntityPathBase<PetLike> {

    private static final long serialVersionUID = -277322961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetLike petLike = new QPetLike("petLike");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.withdogandcat.domain.pet.entity.QPet pet;

    public final com.example.withdogandcat.domain.user.entity.QUser user;

    public QPetLike(String variable) {
        this(PetLike.class, forVariable(variable), INITS);
    }

    public QPetLike(Path<? extends PetLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetLike(PathMetadata metadata, PathInits inits) {
        this(PetLike.class, metadata, inits);
    }

    public QPetLike(Class<? extends PetLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new com.example.withdogandcat.domain.pet.entity.QPet(forProperty("pet"), inits.get("pet")) : null;
        this.user = inits.isInitialized("user") ? new com.example.withdogandcat.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

