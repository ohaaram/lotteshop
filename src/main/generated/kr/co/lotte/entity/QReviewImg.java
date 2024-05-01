package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewImg is a Querydsl query type for ReviewImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewImg extends EntityPathBase<ReviewImg> {

    private static final long serialVersionUID = -1043114553L;

    public static final QReviewImg reviewImg = new QReviewImg("reviewImg");

    public final StringPath oName = createString("oName");

    public final NumberPath<Integer> rno = createNumber("rno", Integer.class);

    public final NumberPath<Integer> rwno = createNumber("rwno", Integer.class);

    public final StringPath sName = createString("sName");

    public QReviewImg(String variable) {
        super(ReviewImg.class, forVariable(variable));
    }

    public QReviewImg(Path<? extends ReviewImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewImg(PathMetadata metadata) {
        super(ReviewImg.class, metadata);
    }

}

