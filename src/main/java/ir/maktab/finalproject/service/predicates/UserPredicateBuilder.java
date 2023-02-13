package ir.maktab.finalproject.service.predicates;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPredicateBuilder {
    private List<SearchCriteria> params;
    private Class aClass;
    private String className;

    public UserPredicateBuilder(Class aClass,String className) {
        this.aClass = aClass;
        this.className = className;
        params = new ArrayList<>();
    }

    public UserPredicateBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream().map(param -> {
            UserPredicate predicate = new UserPredicate(param);
            return predicate.getPredicate(aClass,className);
        }).filter(Objects::nonNull).toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
