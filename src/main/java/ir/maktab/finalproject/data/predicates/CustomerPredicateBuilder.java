package ir.maktab.finalproject.data.predicates;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerPredicateBuilder {
    private List<SearchCriteria> params;

    public CustomerPredicateBuilder() {
        params = new ArrayList<>();
    }

    public CustomerPredicateBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream().map(param -> {
            CustomerPredicate predicate = new CustomerPredicate(param);
            return predicate.getPredicate();
        }).filter(Objects::nonNull).toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
