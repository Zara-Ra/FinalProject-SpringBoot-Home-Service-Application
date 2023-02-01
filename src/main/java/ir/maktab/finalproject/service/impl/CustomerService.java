package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.enums.Role;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.util.validation.Validation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements IRolesService<Customer> {
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
    public Customer changePassword(String email, String oldPassword, String newPassword) {
        Customer findCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No Username Registerd With This Email"));

        if (!findCustomer.getPassword().equals(oldPassword))
            throw new PasswordException("Entered Password Doesn't Match");

        Validation.validatePassword(newPassword);
        findCustomer.setPassword(newPassword);
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
}

