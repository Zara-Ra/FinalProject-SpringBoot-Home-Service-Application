package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.enums.Role;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.CreditException;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.service.predicates.user.UserPredicateBuilder;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.validation.Validation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService extends MainService implements IRolesService<Customer> {
    private final CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer register(Customer customer) {
        validateNewCustomer(customer);
        customer.setCredit(Credit.builder().amount(0).build());
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(messageSource.getMessage("errors.message.duplicate_user"));
        }
    }

    /*@Override
    public Customer signIn(String email, String password) {
        validateAccount(email, password);
        Customer foundCustomer = customerRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(messageSource.getMessage("errors.message.customer_not_exists")));

        if (!foundCustomer.getPassword().equals(password))
            throw new UserNotFoundException(messageSource.getMessage("errors.message.invalid_password"));

        return foundCustomer;
    }
*/
    @Override
    public Customer changePassword(AccountDto accountDto) {
        if (!accountDto.getNewPassword().equals(accountDto.getRepeatPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.password_mismatch"));

        Customer findCustomer = customerRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.customer_not_exists")));

        if(!passwordEncoder.matches(accountDto.getOldPassword(), findCustomer.getPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.incorrect_old_password"));

        Validation.validatePassword(accountDto.getNewPassword());
        findCustomer.setPassword(passwordEncoder.encode(accountDto.getNewPassword()));
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
            throw new CreditException(messageSource.getMessage("errors.message.insufficient_credit"));
        customer.getCredit().setAmount(creditAmount - payAmount);
        customerRepository.save(customer);
    }

    public void increaseCredit(String customerEmail, double amount) {
        Customer customer = findByEmail(customerEmail)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.customer_not_exists")));
        customer.getCredit().setAmount(amount);
        customerRepository.save(customer);
    }

    public Iterable<Customer> findAll(String search) {
        if (search.isEmpty())
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_null_search"));

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

