package com.example.withdogandcat.domain.hashtag.chattag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatRoomTag is a Querydsl query type for ChatRoomTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomTag extends EntityPathBase<ChatRoomTag> {

    private static final long serialVersionUID = -1841829959L;

    public static final QChatRoomTag chatRoomTag = new QChatRoomTag("chatRoomTag");

    public final com.example.withdogandcat.global.common.QTimestamped _super = new com.example.withdogandcat.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QChatRoomTag(String variable) {
        super(ChatRoomTag.class, forVariable(variable));
    }

    public QChatRoomTag(Path<? extends ChatRoomTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoomTag(PathMetadata metadata) {
        super(ChatRoomTag.class, metadata);
    }

}

