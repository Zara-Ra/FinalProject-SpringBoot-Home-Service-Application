package ir.maktab.finalproject.data.entity.services;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseService is a Querydsl query type for BaseService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBaseService extends EntityPathBase<BaseService> {

    private static final long serialVersionUID = -644375013L;

    public static final QBaseService baseService = new QBaseService("baseService");

    public final StringPath baseName = createString("baseName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QBaseService(String variable) {
        super(BaseService.class, forVariable(variable));
    }

    public QBaseService(Path<? extends BaseService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseService(PathMetadata metadata) {
        super(BaseService.class, metadata);
    }

}

