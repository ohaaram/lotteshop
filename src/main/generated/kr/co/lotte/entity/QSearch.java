package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearch is a Querydsl query type for Search
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearch extends EntityPathBase<Search> {

    private static final long serialVersionUID = 595430060L;

    public static final QSearch search = new QSearch("search");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath keyword = createString("keyword");

    public QSearch(String variable) {
        super(Search.class, forVariable(variable));
    }

    public QSearch(Path<? extends Search> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearch(PathMetadata metadata) {
        super(Search.class, metadata);
    }

}

