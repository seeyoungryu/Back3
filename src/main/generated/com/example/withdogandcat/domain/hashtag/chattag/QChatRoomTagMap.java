package com.example.withdogandcat.domain.hashtag.chattag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomTagMap is a Querydsl query type for ChatRoomTagMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomTagMap extends EntityPathBase<ChatRoomTagMap> {

    private static final long serialVersionUID = -1749025053L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoomTagMap chatRoomTagMap = new QChatRoomTagMap("chatRoomTagMap");

    public final com.example.withdogandcat.global.common.QTimestamped _super = new com.example.withdogandcat.global.common.QTimestamped(this);

    public final com.example.withdogandcat.domain.chat.entity.QChatRoomEntity chatRoom;

    public final QChatRoomTag chatRoomTag;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QChatRoomTagMap(String variable) {
        this(ChatRoomTagMap.class, forVariable(variable), INITS);
    }

    public QChatRoomTagMap(Path<? extends ChatRoomTagMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoomTagMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoomTagMap(PathMetadata metadata, PathInits inits) {
        this(ChatRoomTagMap.class, metadata, inits);
    }

    public QChatRoomTagMap(Class<? extends ChatRoomTagMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.example.withdogandcat.domain.chat.entity.QChatRoomEntity(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.chatRoomTag = inits.isInitialized("chatRoomTag") ? new QChatRoomTag(forProperty("chatRoomTag")) : null;
    }

}

