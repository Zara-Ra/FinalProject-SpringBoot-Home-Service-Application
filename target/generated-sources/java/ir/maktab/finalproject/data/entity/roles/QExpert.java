package ir.maktab.finalproject.data.entity.roles;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExpert is a Querydsl query type for Expert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExpert extends EntityPathBase<Expert> {

    private static final long serialVersionUID = -895026494L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExpert expert = new QExpert("expert");

    public final QUser _super;

    public final ListPath<ir.maktab.finalproject.data.entity.ExpertOffer, ir.maktab.finalproject.data.entity.QExpertOffer> acceptedOfferList = this.<ir.maktab.finalproject.data.entity.ExpertOffer, ir.maktab.finalproject.data.entity.QExpertOffer>createList("acceptedOfferList", ir.maktab.finalproject.data.entity.ExpertOffer.class, ir.maktab.finalproject.data.entity.QExpertOffer.class, PathInits.DIRECT2);

    public final NumberPath<Double> averageScore = createNumber("averageScore", Double.class);

    // inherited
    public final ir.maktab.finalproject.data.entity.QCredit credit;

    //inherited
    public final StringPath email;

    //inherited
    public final BooleanPath enabled;

    //inherited
    public final StringPath firstName;

    //inherited
    public final NumberPath<Integer> id;

    //inherited
    public final StringPath lastName;

    //inherited
    public final StringPath password;

    public final ArrayPath<byte[], Byte> photo = createArray("photo", byte[].class);

    //inherited
    public final DatePath<java.util.Date> registerDate;

    public final ListPath<ir.maktab.finalproject.data.entity.Review, ir.maktab.finalproject.data.entity.QReview> reviewList = this.<ir.maktab.finalproject.data.entity.Review, ir.maktab.finalproject.data.entity.QReview>createList("reviewList", ir.maktab.finalproject.data.entity.Review.class, ir.maktab.finalproject.data.entity.QReview.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<ir.maktab.finalproject.data.enums.Role> role;

    public final EnumPath<ir.maktab.finalproject.data.enums.ExpertStatus> status = createEnum("status", ir.maktab.finalproject.data.enums.ExpertStatus.class);

    public final ListPath<ir.maktab.finalproject.data.entity.services.SubService, ir.maktab.finalproject.data.entity.services.QSubService> subServiceList = this.<ir.maktab.finalproject.data.entity.services.SubService, ir.maktab.finalproject.data.entity.services.QSubService>createList("subServiceList", ir.maktab.finalproject.data.entity.services.SubService.class, ir.maktab.finalproject.data.entity.services.QSubService.class, PathInits.DIRECT2);

    public final StringPath verificationCode = createString("verificationCode");

    public QExpert(String variable) {
        this(Expert.class, forVariable(variable), INITS);
    }

    public QExpert(Path<? extends Expert> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExpert(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExpert(PathMetadata metadata, PathInits inits) {
        this(Expert.class, metadata, inits);
    }

    public QExpert(Class<? extends Expert> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QUser(type, metadata, inits);
        this.credit = _super.credit;
        this.email = _super.email;
        this.enabled = _super.enabled;
        this.firstName = _super.firstName;
        this.id = _super.id;
        this.lastName = _super.lastName;
        this.password = _super.password;
        this.registerDate = _super.registerDate;
        this.role = _super.role;
    }

}

