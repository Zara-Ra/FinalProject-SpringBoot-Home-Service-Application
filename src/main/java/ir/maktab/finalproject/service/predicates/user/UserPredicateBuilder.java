package ir.maktab.finalproject.service.predicates.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPredicateBuilder {
    private List<SearchCriteria> params;

    public UserPredicateBuilder() {
        params = new ArrayList<>();
    }

    public void with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
    }

    public BooleanExpression build(Class aClass, String className) {
        if (params.size() == 0)
            return null;

        List<BooleanExpression> predicates = params.stream().map(param -> {
            UserPredicate predicate = new UserPredicate(param);
            return predicate.getPredicate(aClass, className);
        }).filter(Objects::nonNull).toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}