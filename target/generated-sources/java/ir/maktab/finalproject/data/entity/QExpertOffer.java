package ir.maktab.finalproject.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExpertOffer is a Querydsl query type for ExpertOffer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExpertOffer extends EntityPathBase<ExpertOffer> {

    private static final long serialVersionUID = 84829483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExpertOffer expertOffer = new QExpertOffer("expertOffer");

    public final QCustomerOrder customerOrder;

    public final ComparablePath<java.time.Duration> duration = createComparable("duration", java.time.Duration.class);

    public final ir.maktab.finalproject.data.entity.roles.QExpert expert;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isChosen = createBoolean("isChosen");

    public final DateTimePath<java.util.Date> preferredDate = createDateTime("preferredDate", java.util.Date.class);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final DatePath<java.util.Date> registerDate = createDate("registerDate", java.util.Date.class);

    public final ir.maktab.finalproject.data.entity.services.QSubService subService;

    public QExpertOffer(String variable) {
        this(ExpertOffer.class, forVariable(variable), INITS);
    }

    public QExpertOffer(Path<? extends ExpertOffer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExpertOffer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExpertOffer(PathMetadata metadata, PathInits inits) {
        this(ExpertOffer.class, metadata, inits);
    }

    public QExpertOffer(Class<? extends ExpertOffer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customerOrder = inits.isInitialized("customerOrder") ? new QCustomerOrder(forProperty("customerOrder"), inits.get("customerOrder")) : null;
        this.expert = inits.isInitialized("expert") ? new ir.maktab.finalproject.data.entity.roles.QExpert(forProperty("expert"), inits.get("expert")) : null;
        this.subService = inits.isInitialized("subService") ? new ir.maktab.finalproject.data.entity.services.QSubService(forProperty("subService"), inits.get("subService")) : null;
    }

}

