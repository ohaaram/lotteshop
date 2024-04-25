package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCsFaq is a Querydsl query type for CsFaq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCsFaq extends EntityPathBase<CsFaq> {

    private static final long serialVersionUID = -2073388286L;

    public static final QCsFaq csFaq = new QCsFaq("csFaq");

    public final StringPath cate1 = createString("cate1");

    public final StringPath cate2 = createString("cate2");

    public final StringPath catename = createString("catename");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final StringPath title = createString("title");

    public QCsFaq(String variable) {
        super(CsFaq.class, forVariable(variable));
    }

    public QCsFaq(Path<? extends CsFaq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCsFaq(PathMetadata metadata) {
        super(CsFaq.class, metadata);
    }

}

