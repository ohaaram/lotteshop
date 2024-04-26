package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecruit is a Querydsl query type for Recruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruit extends EntityPathBase<Recruit> {

    private static final long serialVersionUID = 392823482L;

    public static final QRecruit recruit = new QRecruit("recruit");

    public final StringPath career = createString("career");

    public final StringPath employment_type = createString("employment_type");

    public final StringPath period = createString("period");

    public final StringPath r_cate = createString("r_cate");

    public final StringPath r_content = createString("r_content");

    public final NumberPath<Integer> r_no = createNumber("r_no", Integer.class);

    public final StringPath r_title = createString("r_title");

    public QRecruit(String variable) {
        super(Recruit.class, forVariable(variable));
    }

    public QRecruit(Path<? extends Recruit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecruit(PathMetadata metadata) {
        super(Recruit.class, metadata);
    }

}

