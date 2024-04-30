package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeller_status is a Querydsl query type for Seller_status
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller_status extends EntityPathBase<Seller_status> {

    private static final long serialVersionUID = -485830866L;

    public static final QSeller_status seller_status = new QSeller_status("seller_status");

    public final NumberPath<Integer> orderCount = createNumber("orderCount", Integer.class);

    public final StringPath sellerUid = createString("sellerUid");

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QSeller_status(String variable) {
        super(Seller_status.class, forVariable(variable));
    }

    public QSeller_status(Path<? extends Seller_status> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeller_status(PathMetadata metadata) {
        super(Seller_status.class, metadata);
    }

}

