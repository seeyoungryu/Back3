package com.example.withdogandcat.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatMessageEntity is a Querydsl query type for ChatMessageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatMessageEntity extends EntityPathBase<ChatMessageEntity> {

    private static final long serialVersionUID = 1082036253L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatMessageEntity chatMessageEntity = new QChatMessageEntity("chatMessageEntity");

    public final com.example.withdogandcat.global.common.QTimestamped _super = new com.example.withdogandcat.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final StringPath roomId = createString("roomId");

    public final com.example.withdogandcat.domain.user.entity.QUser sender;

    public final EnumPath<MessageType> type = createEnum("type", MessageType.class);

    public QChatMessageEntity(String variable) {
        this(ChatMessageEntity.class, forVariable(variable), INITS);
    }

    public QChatMessageEntity(Path<? extends ChatMessageEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatMessageEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatMessageEntity(PathMetadata metadata, PathInits inits) {
        this(ChatMessageEntity.class, metadata, inits);
    }

    public QChatMessageEntity(Class<? extends ChatMessageEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sender = inits.isInitialized("sender") ? new com.example.withdogandcat.domain.user.entity.QUser(forProperty("sender")) : null;
    }

}

