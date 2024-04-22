package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = 595752067L;

    public static final QSeller seller = new QSeller("seller");

    public final StringPath sellerAddr1 = createString("sellerAddr1");

    public final StringPath sellerAddr2 = createString("sellerAddr2");

    public final StringPath sellerCEO = createString("sellerCEO");

    public final StringPath sellerDHp = createString("sellerDHp");

    public final StringPath sellerDname = createString("sellerDname");

    public final StringPath sellerEmail = createString("sellerEmail");

    public final StringPath sellerFax = createString("sellerFax");

    public final StringPath sellerHp = createString("sellerHp");

    public final StringPath sellerName = createString("sellerName");

    public final StringPath sellerPass = createString("sellerPass");

    public final StringPath sellerSaNumber = createString("sellerSaNumber");

    public final StringPath sellerTongNumber = createString("sellerTongNumber");

    public final StringPath sellerUid = createString("sellerUid");

    public final StringPath sellerZip = createString("sellerZip");

    public QSeller(String variable) {
        super(Seller.class, forVariable(variable));
    }

    public QSeller(Path<? extends Seller> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeller(PathMetadata metadata) {
        super(Seller.class, metadata);
    }

}

