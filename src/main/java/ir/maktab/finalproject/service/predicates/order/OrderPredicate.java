package ir.maktab.finalproject.service.predicates.order;

import com.querydsl.core.types.dsl.*;
import ir.maktab.finalproject.data.entity.QCustomerOrder;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderPredicate {
    private SearchCriteria criteria;

    public OrderPredicate(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate() {
        if (criteria.getKey().equals("customerEmail")) {
            StringPath path = QCustomerOrder.customerOrder.customer.email;
            return path.eq(criteria.getValue().toString());
        }
        if (criteria.getKey().equals("subServiceName")) {
            return subServiceBooleanExpression();
        }
        if (criteria.getKey().equals("expertEmail")) {
            StringPath path = QCustomerOrder.customerOrder.acceptedExpertOffer.expert.email;
            return path.eq(criteria.getValue().toString());
        }
        if (criteria.getKey().equals("offerId")) {
            NumberPath<Integer> path = QCustomerOrder.customerOrder.acceptedExpertOffer.id;
            return intBooleanExpression(path);
        }
        if (criteria.getKey().equals("offerPrice")) {
            NumberPath<Double> path = QCustomerOrder.customerOrder.acceptedExpertOffer.price;
            return doubleBooleanExpression(path);
        }
        if (criteria.getKey().equals("status")) {
            return statusBooleanExpression();
        }
        if (criteria.getKey().equals("startDate")) {
            DateTimePath<Date> path = QCustomerOrder.customerOrder.startDate;
            return dateBooleanExpression(path);
        }
        if (criteria.getKey().equals("finishDate")) {
            DateTimePath<Date> path = QCustomerOrder.customerOrder.finishDate;
            return dateBooleanExpression(path);
        }

        return null;
    }

    private BooleanExpression statusBooleanExpression() {
        EnumPath<OrderStatus> path = QCustomerOrder.customerOrder.status;
        return path.eq(OrderStatus.valueOf(criteria.getValue().toString()));
    }

    private BooleanExpression subServiceBooleanExpression() {
        StringPath path = QCustomerOrder.customerOrder.subService.subName;
        return path.eq(criteria.getValue().toString());
    }

    private BooleanExpression intBooleanExpression(NumberPath<Integer> path) {
        int value = Integer.parseInt(criteria.getValue().toString());
        return switch (criteria.getOperation()) {
            case ":" -> path.eq(value);
            case ">" -> path.goe(value);
            case "<" -> path.loe(value);
            default -> null;
        };
    }

    private BooleanExpression doubleBooleanExpression(NumberPath<Double> path) {
        double value = Double.parseDouble(criteria.getValue().toString());
        return switch (criteria.getOperation()) {
            case ":" -> path.eq(value);
            case ">" -> path.goe(value);
            case "<" -> path.loe(value);
            default -> null;
        };
    }

    private BooleanExpression dateBooleanExpression(DateTimePath<Date> path) {
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
}
