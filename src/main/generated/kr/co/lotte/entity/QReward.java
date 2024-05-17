package kr.co.lotte.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReward is a Querydsl query type for Reward
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReward extends EntityPathBase<Reward> {

    private static final long serialVersionUID = 567440435L;

    public static final QReward reward = new QReward("reward");

    public final StringPath date = createString("date");

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final StringPath uid = createString("uid");

    public QReward(String variable) {
        super(Reward.class, forVariable(variable));
    }

    public QReward(Path<? extends Reward> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReward(PathMetadata metadata) {
        super(Reward.class, metadata);
    }

}

