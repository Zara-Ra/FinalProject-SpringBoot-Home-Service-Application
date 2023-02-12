package ir.maktab.finalproject.data.entity.roles;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = 1414448758L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomer customer = new QCustomer("customer");

    public final QUser _super;

    // inherited
    public final ir.maktab.finalproject.data.entity.QCredit credit;

    public final ListPath<ir.maktab.finalproject.data.entity.CustomerOrder, ir.maktab.finalproject.data.entity.QCustomerOrder> customerOrderList = this.<ir.maktab.finalproject.data.entity.CustomerOrder, ir.maktab.finalproject.data.entity.QCustomerOrder>createList("customerOrderList", ir.maktab.finalproject.data.entity.CustomerOrder.class, ir.maktab.finalproject.data.entity.QCustomerOrder.class, PathInits.DIRECT2);

    //inherited
    public final StringPath email;

    //inherited
    public final StringPath firstName;

    //inherited
    public final NumberPath<Integer> id;

    //inherited
    public final StringPath lastName;

    //inherited
    public final StringPath password;

    //inherited
    public final DatePath<java.util.Date> registerDate;

    //inherited
    public final EnumPath<ir.maktab.finalproject.data.entity.roles.enums.Role> role;

    public QCustomer(String variable) {
        this(Customer.class, forVariable(variable), INITS);
    }

    public QCustomer(Path<? extends Customer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomer(PathMetadata metadata, PathInits inits) {
        this(Customer.class, metadata, inits);
    }

    public QCustomer(Class<? extends Customer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QUser(type, metadata, inits);
        this.credit = _super.credit;
        this.email = _super.email;
        this.firstName = _super.firstName;
        this.id = _super.id;
        this.lastName = _super.lastName;
        this.password = _super.password;
        this.registerDate = _super.registerDate;
        this.role = _super.role;
    }

}

