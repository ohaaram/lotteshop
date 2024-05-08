package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDownloadCoupon is a Querydsl query type for DownloadCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDownloadCoupon extends EntityPathBase<DownloadCoupon> {

    private static final long serialVersionUID = 1719396306L;

    public static final QDownloadCoupon downloadCoupon = new QDownloadCoupon("downloadCoupon");

    public final NumberPath<Integer> couponCode = createNumber("couponCode", Integer.class);

    public final NumberPath<Integer> pk = createNumber("pk", Integer.class);

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public final StringPath uid = createString("uid");

    public QDownloadCoupon(String variable) {
        super(DownloadCoupon.class, forVariable(variable));
    }

    public QDownloadCoupon(Path<? extends DownloadCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDownloadCoupon(PathMetadata metadata) {
        super(DownloadCoupon.class, metadata);
    }

}

