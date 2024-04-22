package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubProducts is a Querydsl query type for SubProducts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubProducts extends EntityPathBase<SubProducts> {

    private static final long serialVersionUID = 1560613184L;

    public static final QSubProducts subProducts = new QSubProducts("subProducts");

    public final StringPath color = createString("color");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final NumberPath<Integer> prodPrice = createNumber("prodPrice", Integer.class);

    public final NumberPath<Integer> prodSold = createNumber("prodSold", Integer.class);

    public final NumberPath<Integer> prodStock = createNumber("prodStock", Integer.class);

    public final StringPath size = createString("size");

    public final NumberPath<Integer> subProdNo = createNumber("subProdNo", Integer.class);

    public QSubProducts(String variable) {
        super(SubProducts.class, forVariable(variable));
    }

    public QSubProducts(Path<? extends SubProducts> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubProducts(PathMetadata metadata) {
        super(SubProducts.class, metadata);
    }

}

