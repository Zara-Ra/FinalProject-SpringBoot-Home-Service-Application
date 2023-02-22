package ir.maktab.finalproject.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCredit is a Querydsl query type for Credit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCredit extends EntityPathBase<Credit> {

    private static final long serialVersionUID = 1303128192L;

    public static final QCredit credit = new QCredit("credit");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QCredit(String variable) {
        super(Credit.class, forVariable(variable));
    }

    public QCredit(Path<? extends Credit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCredit(PathMetadata metadata) {
        super(Credit.class, metadata);
    }

}

