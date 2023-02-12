package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.data.entity.roles.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> ,
        QuerydslPredicateExecutor<Customer> {
    Optional<Customer> findByEmail(String email);
}
