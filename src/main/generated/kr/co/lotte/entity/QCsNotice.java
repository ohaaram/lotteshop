package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCsNotice is a Querydsl query type for CsNotice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCsNotice extends EntityPathBase<CsNotice> {

    private static final long serialVersionUID = -2143588500L;

    public static final QCsNotice csNotice = new QCsNotice("csNotice");

    public final StringPath cate1 = createString("cate1");

    public final StringPath cate2 = createString("cate2");

    public final StringPath catename = createString("catename");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> file = createNumber("file", Integer.class);

    public final ListPath<File, QFile> fileList = this.<File, QFile>createList("fileList", File.class, QFile.class, PathInits.DIRECT2);

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath regip = createString("regip");

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QCsNotice(String variable) {
        super(CsNotice.class, forVariable(variable));
    }

    public QCsNotice(Path<? extends CsNotice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCsNotice(PathMetadata metadata) {
        super(CsNotice.class, metadata);
    }

}

