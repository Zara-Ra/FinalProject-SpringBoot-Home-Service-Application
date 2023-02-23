package ir.maktab.finalproject.service.search.predicates.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ir.maktab.finalproject.service.search.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderPredicateBuilder {
    private List<SearchCriteria> params;

    public OrderPredicateBuilder() {
        params = new ArrayList<>();
    }

    public void with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
    }

    public BooleanExpression build() {
        if (params.size() == 0)
            return null;

        List<BooleanExpression> predicates = params.stream().map(param -> {
            OrderPredicate predicate = new OrderPredicate(param);
            return predicate.getPredicate();
        }).toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            if(predicate == null)
                return Expressions.asBoolean(false).isTrue();
            result = result.and(predicate);
        }
        return result;
    }
}
