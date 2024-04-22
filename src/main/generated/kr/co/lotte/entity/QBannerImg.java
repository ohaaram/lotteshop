package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBannerImg is a Querydsl query type for BannerImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBannerImg extends EntityPathBase<BannerImg> {

    private static final long serialVersionUID = 1062980979L;

    public static final QBannerImg bannerImg = new QBannerImg("bannerImg");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final NumberPath<Integer> ino = createNumber("ino", Integer.class);

    public final StringPath oName = createString("oName");

    public final StringPath sName = createString("sName");

    public QBannerImg(String variable) {
        super(BannerImg.class, forVariable(variable));
    }

    public QBannerImg(Path<? extends BannerImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBannerImg(PathMetadata metadata) {
        super(BannerImg.class, metadata);
    }

}

