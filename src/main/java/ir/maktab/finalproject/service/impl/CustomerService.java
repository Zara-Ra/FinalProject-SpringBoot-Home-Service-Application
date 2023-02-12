package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.QCustomer;
import ir.maktab.finalproject.data.entity.roles.enums.Role;
import ir.maktab.finalproject.data.predicates.CustomerPredicateBuilder;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.exception.CreditException;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService implements IRolesService<Customer>/*, CommandLineRunner*/ {
    private final CustomerRepository customerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer signUp(Customer customer) {
        validateNewCustomer(customer);
        customer.setCredit(Credit.builder().amount(0).build());
        customer.setRole(Role.ROLE_CUSTOMER);
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Already Registered With This Email");
        }
    }

    @Override
    public Customer signIn(String email, String password) {
        validateAccount(email, password);
        Customer foundCustomer = customerRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("No User Registered With This Email"));

        if (!foundCustomer.getPassword().equals(password))
            throw new UserNotFoundException("Incorrect Password");

        return foundCustomer;
    }

    @Override
    public Customer changePassword(AccountDto accountDto) {
        if (!accountDto.getNewPassword().equals(accountDto.getRepeatPassword()))
            throw new PasswordException("New Password And Repeat Password Don't Match");

        Customer findCustomer = customerRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No Username Registered With This Email"));

        if (!findCustomer.getPassword().equals(accountDto.getPassword()))
            throw new PasswordException("Incorrect Old Password");

        Validation.validatePassword(accountDto.getNewPassword());
        findCustomer.setPassword(accountDto.getNewPassword());
        return customerRepository.save(findCustomer);
    }

    private void validateNewCustomer(Customer customer) {
        Validation.validateName(customer.getFirstName());
        Validation.validateName(customer.getLastName());
        validateAccount(customer.getEmail(), customer.getPassword());
    }

    private void validateAccount(String email, String password) {
        Validation.validateEmail(email);
        Validation.validatePassword(password);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void pay(Customer customer, double payAmount) {
        double creditAmount = customer.getCredit().getAmount();
        if (creditAmount < payAmount)
            throw new CreditException("Credit Not Enough");
        customer.getCredit().setAmount(creditAmount - payAmount);
        customerRepository.save(customer);
    }

    public void increaseCredit(String customerEmail, double amount) {
        Customer customer = findByEmail(customerEmail)
                .orElseThrow(()-> new UserNotFoundException("Customer Not Exists"));
        customer.getCredit().setAmount(amount);
        customerRepository.save(customer);
    }

    public Iterable<Customer> findAll(String search) {
        CustomerPredicateBuilder builder = new CustomerPredicateBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression expression = builder.build();
        return customerRepository.findAll(expression);
    }

    /*public void run(String... args) throws Exception {
        *//*var qCustomer = QCustomer.customer;
        var query = new JPAQuery(entityManager);
        query.from(qCustomer).where(qCustomer.firstName.eq("Customer"));
        var fetch = query.fetch();
        System.out.println(fetch.get(0));*//*

        var qCustomer = QCustomer.customer;
        JPAQuery<QCustomer> query1 = new JPAQuery<>(entityManager);
        query1.from(qCustomer).where(qCustomer.firstName.eq("Zahra"));
        System.out.println(query1.fetch().size());

    }*/
}

