package ir.maktab.finalproject.data.entity.roles;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = 105382839L;

    public static final QAdmin admin = new QAdmin("admin");

    public final QAccount _super = new QAccount(this);

    //inherited
    public final StringPath email = _super.email;

    public final BooleanPath enabled = createBoolean("enabled");

    //inherited
    public final NumberPath<Integer> id = _super.id;

    //inherited
    public final StringPath password = _super.password;

    //inherited
    public final EnumPath<ir.maktab.finalproject.data.entity.roles.enums.Role> role = _super.role;

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

