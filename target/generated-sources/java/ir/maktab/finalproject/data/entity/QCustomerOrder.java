package ir.maktab.finalproject.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerOrder is a Querydsl query type for CustomerOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerOrder extends EntityPathBase<CustomerOrder> {

    private static final long serialVersionUID = 553246377L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomerOrder customerOrder = new QCustomerOrder("customerOrder");

    public final QExpertOffer acceptedExpertOffer;

    public final QAddress address;

    public final ir.maktab.finalproject.data.entity.roles.QCustomer customer;

    public final StringPath description = createString("description");

    public final ListPath<ExpertOffer, QExpertOffer> expertOfferList = this.<ExpertOffer, QExpertOffer>createList("expertOfferList", ExpertOffer.class, QExpertOffer.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> finishDate = createDateTime("finishDate", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> preferredDate = createDateTime("preferredDate", java.util.Date.class);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final EnumPath<ir.maktab.finalproject.data.enums.OrderStatus> status = createEnum("status", ir.maktab.finalproject.data.enums.OrderStatus.class);

    public final QSubService subService;

    public QCustomerOrder(String variable) {
        this(CustomerOrder.class, forVariable(variable), INITS);
    }

    public QCustomerOrder(Path<? extends CustomerOrder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomerOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomerOrder(PathMetadata metadata, PathInits inits) {
        this(CustomerOrder.class, metadata, inits);
    }

    public QCustomerOrder(Class<? extends CustomerOrder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.acceptedExpertOffer = inits.isInitialized("acceptedExpertOffer") ? new QExpertOffer(forProperty("acceptedExpertOffer"), inits.get("acceptedExpertOffer")) : null;
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.customer = inits.isInitialized("customer") ? new ir.maktab.finalproject.data.entity.roles.QCustomer(forProperty("customer"), inits.get("customer")) : null;
        this.subService = inits.isInitialized("subService") ? new QSubService(forProperty("subService"), inits.get("subService")) : null;
    }

}

