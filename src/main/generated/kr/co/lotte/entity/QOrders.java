package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = 492996585L;

    public static final QOrders orders = new QOrders("orders");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final NumberPath<Integer> couponCode = createNumber("couponCode", Integer.class);

    public final NumberPath<Integer> couponDiscount = createNumber("couponDiscount", Integer.class);

    public final NumberPath<Integer> delivery = createNumber("delivery", Integer.class);

    public final NumberPath<Integer> itemDiscount = createNumber("itemDiscount", Integer.class);

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.util.Date> orderDate = createDateTime("orderDate", java.util.Date.class);

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final NumberPath<Integer> orderTotalPrice = createNumber("orderTotalPrice", Integer.class);

    public final StringPath payment = createString("payment");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath receiveHp = createString("receiveHp");

    public final StringPath receiveName = createString("receiveName");

    public final StringPath sendHp = createString("sendHp");

    public final StringPath sendName = createString("sendName");

    public final StringPath userId = createString("userId");

    public final StringPath zip = createString("zip");

    public QOrders(String variable) {
        super(Orders.class, forVariable(variable));
    }

    public QOrders(Path<? extends Orders> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrders(PathMetadata metadata) {
        super(Orders.class, metadata);
    }

}

