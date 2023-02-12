package ir.maktab.finalproject.data.predicates;

import com.querydsl.core.types.dsl.*;
import ir.maktab.finalproject.data.entity.QCredit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerPredicate {
    private SearchCriteria criteria;

    public CustomerPredicate(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate() {
        PathBuilder<Customer> entityPath = new PathBuilder<>(Customer.class, "customer");

        if (criteria.getKey().contains("Date")) {
            return dateBooleanExpression(entityPath);
        }
        if (criteria.getKey().contains("credit")) {
            return creditBooleanExpression();
        }
        //if (isNumeric(criteria.getValue().toString())) {
        if(criteria.getKey().contains("id")){
            return intBooleanExpression(entityPath);
        }
        StringPath path = entityPath.getString(criteria.getKey());
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return path.containsIgnoreCase(criteria.getValue().toString());
        }
        return null;
    }

    private BooleanExpression intBooleanExpression(PathBuilder<Customer> entityPath) {
        NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
        int value = Integer.parseInt(criteria.getValue().toString());
        return switch (criteria.getOperation()) {
            case ":" -> path.eq(value);
            case ">" -> path.goe(value);
            case "<" -> path.loe(value);
            default -> null;
        };
    }

    private BooleanExpression dateBooleanExpression(PathBuilder<Customer> entityPath){
        DatePath<Date> path = entityPath.getDate(criteria.getKey(), Date.class);
        String operation = criteria.getOperation();
        try {
            return switch (operation) {
                case ":" -> path.eq(
                        new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getValue().toString()));
                case "<" -> path.before(
                        new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getValue().toString()));
                case ">" -> path.after(
                        new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getValue().toString()));
                default -> null;
            };
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date Type(yyyy-MM-dd)");
        }
    }

    private BooleanExpression creditBooleanExpression(){
        QCredit qCredit = QCredit.credit;
        NumberPath<Double> path = qCredit.amount;
        double value = Double.parseDouble(criteria.getValue().toString());
        return switch (criteria.getOperation()) {
            case ":" -> path.eq(value);
            case ">" -> path.goe(value);
            case "<" -> path.loe(value);
            default -> null;
        };
    }
    public static boolean isNumeric(final String str) {
        try {
            Integer.parseInt(str);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }
}
