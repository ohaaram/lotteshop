package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVisitor is a Querydsl query type for Visitor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitor extends EntityPathBase<Visitor> {

    private static final long serialVersionUID = -223105046L;

    public static final QVisitor visitor = new QVisitor("visitor");

    public final NumberPath<Integer> visitCount = createNumber("visitCount", Integer.class);

    public final StringPath visitorDate = createString("visitorDate");

    public QVisitor(String variable) {
        super(Visitor.class, forVariable(variable));
    }

    public QVisitor(Path<? extends Visitor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVisitor(PathMetadata metadata) {
        super(Visitor.class, metadata);
    }

}

