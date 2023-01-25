package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.repository.CustomerRepository;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer signUp(Customer customer) {
        validateNewCustomer(customer);
        customer.setCredit(Credit.builder().amount(0).build());
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Already Registered With This Email");
        }
    }

    public Customer signIn(String email, String password) {
        validateAccount(email, password);
        Customer foundCustomer = customerRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("No User Registered With This Email"));

        if (!foundCustomer.getPassword().equals(password))
            throw new UserNotFoundException("Incorrect Password");

        return foundCustomer;
    }

    public Customer changePassword(Customer customer, String oldPassword, String newPassword) {
        Customer findCustomer = customerRepository.findByEmail(customer.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No Username Registerd With This Email"));

        if (!findCustomer.getPassword().equals(oldPassword))
            throw new PasswordException("Entered Password Doesn't Match");

        Validation.validatePassword(newPassword);
        customer.setPassword(newPassword);
        return customerRepository.save(customer);
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
}

