package ir.maktab.finalproject.data.entity.roles;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 142556771L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final QAccount _super = new QAccount(this);

    public final ir.maktab.finalproject.data.entity.QCredit credit;

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath lastName = createString("lastName");

    //inherited
    public final StringPath password = _super.password;

    public final DatePath<java.util.Date> registerDate = createDate("registerDate", java.util.Date.class);

    //inherited
    public final EnumPath<ir.maktab.finalproject.data.enums.Role> role = _super.role;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.credit = inits.isInitialized("credit") ? new ir.maktab.finalproject.data.entity.QCredit(forProperty("credit")) : null;
    }

}

