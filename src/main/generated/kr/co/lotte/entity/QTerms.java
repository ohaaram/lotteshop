package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTerms is a Querydsl query type for Terms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTerms extends EntityPathBase<Terms> {

    private static final long serialVersionUID = -2058062845L;

    public static final QTerms terms1 = new QTerms("terms1");

    public final NumberPath<Integer> intPk = createNumber("intPk", Integer.class);

    public final StringPath privacy = createString("privacy");

    public final StringPath sms = createString("sms");

    public final StringPath terms = createString("terms");

    public final StringPath terms2 = createString("terms2");

    public QTerms(String variable) {
        super(Terms.class, forVariable(variable));
    }

    public QTerms(Path<? extends Terms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTerms(PathMetadata metadata) {
        super(Terms.class, metadata);
    }

}

