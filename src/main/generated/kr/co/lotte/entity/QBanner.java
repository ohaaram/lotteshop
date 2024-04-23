package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = 105423920L;

    public static final QBanner banner = new QBanner("banner");

    public final NumberPath<Integer> bannerNo = createNumber("bannerNo", Integer.class);

    public final StringPath color = createString("color");

    public final StringPath d_begin = createString("d_begin");

    public final StringPath d_end = createString("d_end");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final StringPath img_1200 = createString("img_1200");

    public final StringPath img_425 = createString("img_425");

    public final StringPath img_456 = createString("img_456");

    public final StringPath img_810 = createString("img_810");

    public final StringPath img_985 = createString("img_985");

    public final StringPath link = createString("link");

    public final StringPath name = createString("name");

    public final StringPath position = createString("position");

    public final StringPath status = createString("status");

    public final StringPath t_begin = createString("t_begin");

    public final StringPath t_end = createString("t_end");

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

