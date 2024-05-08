package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNewTerms is a Querydsl query type for NewTerms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNewTerms extends EntityPathBase<NewTerms> {

    private static final long serialVersionUID = 1455728587L;

    public static final QNewTerms newTerms = new QNewTerms("newTerms");

    public final StringPath chapter1 = createString("chapter1");

    public final StringPath chapter2 = createString("chapter2");

    public final StringPath chapter3 = createString("chapter3");

    public final StringPath chapter4 = createString("chapter4");

    public final StringPath chapter5 = createString("chapter5");

    public final StringPath chapter6 = createString("chapter6");

    public final NumberPath<Integer> intPk = createNumber("intPk", Integer.class);

    public final StringPath name = createString("name");

    public QNewTerms(String variable) {
        super(NewTerms.class, forVariable(variable));
    }

    public QNewTerms(Path<? extends NewTerms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNewTerms(PathMetadata metadata) {
        super(NewTerms.class, metadata);
    }

}

