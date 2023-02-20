package ir.maktab.finalproject.service.predicates.user;

import com.querydsl.core.types.dsl.*;
import ir.maktab.finalproject.data.entity.QCredit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.QCustomer;
import ir.maktab.finalproject.data.entity.roles.QExpert;
import ir.maktab.finalproject.data.entity.roles.User;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.enums.Role;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.search.SearchCriteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPredicate {
    private SearchCriteria criteria;

    public UserPredicate(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate(Class classType, String className) {
        PathBuilder<User> entityPath = new PathBuilder<>(classType, className);
        if (className.equals("customer")) {
            return userExpressions(entityPath);
        } else if (className.equals("expert")) {
            BooleanExpression booleanExpression = userExpressions(entityPath);
            if (booleanExpression != null)
                return booleanExpression;
            if (criteria.getKey().equals("averageScore")) {
                return intBooleanExpression(entityPath);
            }
            if (criteria.getKey().equals("subService")) {
                return subServiceBooleanExpression();
            }
            if (criteria.getKey().equals("status")) {
                return statusBooleanExperssion();
            }
        }
        return null;
    }

    private BooleanExpression userExpressions(PathBuilder<User> entityPath) {
        if (criteria.getKey().equals("registerDate")) {
            return dateBooleanExpression(entityPath);
        }
        if (criteria.getKey().equals("id")) {
            return intBooleanExpression(entityPath);
        }
        if (criteria.getKey().equals("credit")) {
            return creditBooleanExpression();
        }
        if (criteria.getKey().equals("role")) {
            if (entityPath.getType().equals(Customer.class))
                return customerRoleExpression();
            else if (entityPath.getType().equals(Customer.class))
                return expertRoleExpression();
        }
        return null;
    }

    private BooleanExpression statusBooleanExperssion() {
        EnumPath<ExpertStatus> path = QExpert.expert.status;
        return path.eq(ExpertStatus.valueOf(criteria.getValue().toString()));
    }

    private BooleanExpression customerRoleExpression() {
        EnumPath<Role> path = QCustomer.customer.role;
        return path.eq(Role.valueOf(criteria.getValue().toString()));
    }

    private BooleanExpression expertRoleExpression() {
        EnumPath<Role> path = QExpert.expert.role;
        return path.eq(Role.valueOf(criteria.getValue().toString()));
    }

    private BooleanExpression subServiceBooleanExpression() {
        StringPath path = QExpert.expert.subServiceList.any().subName;
        return path.eq(criteria.getValue().toString());
    }

    private BooleanExpression creditBooleanExpression() {
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

    private BooleanExpression intBooleanExpression(PathBuilder<User> entityPath) {
        if (!isInteger(criteria.getValue().toString())) {
            QExpert qExpert = QExpert.expert;
            NumberPath<Double> path = qExpert.averageScore;
            return switch (criteria.getValue().toString()) {
                case "max" -> path.goe(4.5);
                case "min" -> path.loe(0.5);
                default -> null;
            };
        }
        NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
        int value = Integer.parseInt(criteria.getValue().toString());
        return switch (criteria.getOperation()) {
            case ":" -> path.eq(value);
            case ">" -> path.goe(value);
            case "<" -> path.loe(value);
            default -> null;
        };
    }

    private BooleanExpression dateBooleanExpression(PathBuilder<User> entityPath) {
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

    public static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }
}
