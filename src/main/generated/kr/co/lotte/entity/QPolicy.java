package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPolicy is a Querydsl query type for Policy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPolicy extends EntityPathBase<Policy> {

    private static final long serialVersionUID = 519096886L;

    public static final QPolicy policy = new QPolicy("policy");

    public final StringPath chapter1 = createString("chapter1");

    public final StringPath chapter2 = createString("chapter2");

    public final StringPath chapter3 = createString("chapter3");

    public final StringPath chapter4 = createString("chapter4");

    public final StringPath chapter5 = createString("chapter5");

    public final StringPath chapter6 = createString("chapter6");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QPolicy(String variable) {
        super(Policy.class, forVariable(variable));
    }

    public QPolicy(Path<? extends Policy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPolicy(PathMetadata metadata) {
        super(Policy.class, metadata);
    }

}

