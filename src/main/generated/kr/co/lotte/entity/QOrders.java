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

    public final StringPath color = createString("color");

    public final NumberPath<Integer> itemDiscount = createNumber("itemDiscount", Integer.class);

    public final StringPath memo = createString("memo");

    public final StringPath orderAddr = createString("orderAddr");

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final NumberPath<Integer> orderTotalPrice = createNumber("orderTotalPrice", Integer.class);

    public final StringPath receiveHp = createString("receiveHp");

    public final StringPath receiveName = createString("receiveName");

    public final StringPath sendHp = createString("sendHp");

    public final StringPath sendName = createString("sendName");

    public final StringPath size = createString("size");

    public final StringPath userId = createString("userId");

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

