package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.enums.Role;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.exception.CreditException;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.service.predicates.user.UserPredicateBuilder;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.validation.Validation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService implements IRolesService<Customer>/*, CommandLineRunner*/ {
    private final CustomerRepository customerRepository;

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

    public void pay(Customer customer, double payAmount) {
        double creditAmount = customer.getCredit().getAmount();
        if (creditAmount < payAmount)
            throw new CreditException("Credit Not Enough");
        customer.getCredit().setAmount(creditAmount - payAmount);
        customerRepository.save(customer);
    }

    public void increaseCredit(String customerEmail, double amount) {
        Customer customer = findByEmail(customerEmail)
                .orElseThrow(() -> new UserNotFoundException("Customer Not Exists"));
        customer.getCredit().setAmount(amount);
        customerRepository.save(customer);
    }

    public Iterable<Customer> findAll(String search) {
        if (search.isEmpty())
            throw new ValidationException("Search Filter Must Not Be Null");

        UserPredicateBuilder builder = new UserPredicateBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        BooleanExpression expression = builder.build(Customer.class, "customer");
        return customerRepository.findAll(expression);
    }
}

