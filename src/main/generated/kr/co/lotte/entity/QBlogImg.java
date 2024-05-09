package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlogImg is a Querydsl query type for BlogImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlogImg extends EntityPathBase<BlogImg> {

    private static final long serialVersionUID = -711217091L;

    public static final QBlogImg blogImg = new QBlogImg("blogImg");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final NumberPath<Integer> bwno = createNumber("bwno", Integer.class);

    public final StringPath oName = createString("oName");

    public final StringPath sName = createString("sName");

    public QBlogImg(String variable) {
        super(BlogImg.class, forVariable(variable));
    }

    public QBlogImg(Path<? extends BlogImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlogImg(PathMetadata metadata) {
        super(BlogImg.class, metadata);
    }

}

