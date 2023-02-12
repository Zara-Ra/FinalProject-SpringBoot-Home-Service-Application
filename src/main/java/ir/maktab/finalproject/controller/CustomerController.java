package ir.maktab.finalproject.controller;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.CreditDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.data.predicates.CustomerPredicateBuilder;
import ir.maktab.finalproject.service.impl.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody CustomerDto customerDto) {
        customerService.signUp(UserMapper.INSTANCE.convertCustomer(customerDto));
        return "Customer Registered";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AccountDto accountDto) {
        Customer customer = customerService.signIn(accountDto.getEmail(), accountDto.getPassword());
        return "Welcome " + customer.getFirstName() + " " + customer.getLastName();
    }

    @PostMapping("/change_password")
    public String changePassword(@Valid @RequestBody AccountDto accountDto) {
        Customer customer = customerService.changePassword(accountDto);
        return "Password Changed For " + customer.getFirstName() + " " + customer.getLastName();
    }

    @PostMapping("/increase_credit")
    public String increaseCredit(@RequestBody CreditDto creditDto) {
        customerService.increaseCredit(creditDto.getCustomerEmail(), creditDto.getAmount());
        return "Credit Increased ";
    }

    @GetMapping("/filter-user")
    public Iterable<Customer> search(@RequestParam String search) {
        return customerService.findAll(search);
    }

}
