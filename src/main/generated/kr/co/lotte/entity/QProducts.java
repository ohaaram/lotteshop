package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProducts is a Querydsl query type for Products
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProducts extends EntityPathBase<Products> {

    private static final long serialVersionUID = -914766488L;

    public static final QProducts products = new QProducts("products");

    public final NumberPath<Double> avg = createNumber("avg", Double.class);

    public final StringPath cateName1 = createString("cateName1");

    public final StringPath cateName2 = createString("cateName2");

    public final StringPath cateName3 = createString("cateName3");

    public final NumberPath<Integer> cateNo = createNumber("cateNo", Integer.class);

    public final NumberPath<Integer> delivery = createNumber("delivery", Integer.class);

    public final StringPath etc = createString("etc");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final StringPath image1 = createString("image1");

    public final StringPath image2 = createString("image2");

    public final StringPath image3 = createString("image3");

    public final StringPath image4 = createString("image4");

    public final StringPath manufacture = createString("manufacture");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> prodDiscount = createNumber("prodDiscount", Integer.class);

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final NumberPath<Integer> prodPrice = createNumber("prodPrice", Integer.class);

    public final StringPath prodReceipt = createString("prodReceipt");

    public final StringPath prodSa = createString("prodSa");

    public final NumberPath<Integer> prodSold = createNumber("prodSold", Integer.class);

    public final StringPath prodState = createString("prodState");

    public final StringPath prodTax = createString("prodTax");

    public final StringPath prodWonsan = createString("prodWonsan");

    public final NumberPath<Integer> recount = createNumber("recount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> RegProdDate = createDateTime("RegProdDate", java.time.LocalDateTime.class);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final StringPath sellerName = createString("sellerName");

    public final StringPath sellerUid = createString("sellerUid");

    public QProducts(String variable) {
        super(Products.class, forVariable(variable));
    }

    public QProducts(Path<? extends Products> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProducts(PathMetadata metadata) {
        super(Products.class, metadata);
    }

}

