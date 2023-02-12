package ir.maktab.finalproject.data.entity.services;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubService is a Querydsl query type for SubService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubService extends EntityPathBase<SubService> {

    private static final long serialVersionUID = 1313335806L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubService subService = new QSubService("subService");

    public final NumberPath<Double> basePrice = createNumber("basePrice", Double.class);

    public final QBaseService baseService;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath subName = createString("subName");

    public QSubService(String variable) {
        this(SubService.class, forVariable(variable), INITS);
    }

    public QSubService(Path<? extends SubService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubService(PathMetadata metadata, PathInits inits) {
        this(SubService.class, metadata, inits);
    }

    public QSubService(Class<? extends SubService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.baseService = inits.isInitialized("baseService") ? new QBaseService(forProperty("baseService")) : null;
    }

}

