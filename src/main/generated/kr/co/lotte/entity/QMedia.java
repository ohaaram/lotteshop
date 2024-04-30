package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMedia is a Querydsl query type for Media
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedia extends EntityPathBase<Media> {

    private static final long serialVersionUID = -2064541088L;

    public static final QMedia media = new QMedia("media");

    public final StringPath subtitle = createString("subtitle");

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public final NumberPath<Integer> vno = createNumber("vno", Integer.class);

    public QMedia(String variable) {
        super(Media.class, forVariable(variable));
    }

    public QMedia(Path<? extends Media> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMedia(PathMetadata metadata) {
        super(Media.class, metadata);
    }

}

