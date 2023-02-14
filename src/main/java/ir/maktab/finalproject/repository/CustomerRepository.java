package ir.maktab.finalproject.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.QCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>,
        QuerydslPredicateExecutor<Customer>, QuerydslBinderCustomizer<QCustomer> {
    Optional<Customer> findByEmail(String email);

    @Override
    default void customize(
            QuerydslBindings bindings, QCustomer root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
