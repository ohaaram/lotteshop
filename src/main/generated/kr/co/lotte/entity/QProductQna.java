package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductQna is a Querydsl query type for ProductQna
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductQna extends EntityPathBase<ProductQna> {

    private static final long serialVersionUID = 1377671545L;

    public static final QProductQna productQna = new QProductQna("productQna");

    public final StringPath answer = createString("answer");

    public final StringPath cate = createString("cate");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath rdate = createString("rdate");

    public final StringPath sellerUid = createString("sellerUid");

    public final StringPath status1 = createString("status1");

    public final StringPath status2 = createString("status2");

    public final StringPath uid = createString("uid");

    public QProductQna(String variable) {
        super(ProductQna.class, forVariable(variable));
    }

    public QProductQna(Path<? extends ProductQna> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductQna(PathMetadata metadata) {
        super(ProductQna.class, metadata);
    }

}

