package ir.maktab.finalproject.data.entity.roles;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -1843982187L;

    public static final QAccount account = new QAccount("account");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnabled = createBoolean("isEnabled");

    public final StringPath password = createString("password");

    public final EnumPath<ir.maktab.finalproject.data.enums.Role> role = createEnum("role", ir.maktab.finalproject.data.enums.Role.class);

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

