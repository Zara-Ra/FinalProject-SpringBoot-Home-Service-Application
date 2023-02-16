package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.CreditDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.impl.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody CustomerDto customerDto) {
        log.info("*** Add New Customer: {} ***", customerDto);
        Customer customer = customerService.signUp(UserMapper.INSTANCE.convertCustomer(customerDto));
        log.info("*** New Customer Added : {} ***", customer);
        return "Customer Registered Successfully";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AccountDto accountDto) {
        log.info("*** Sign-in Customer: {} ***", accountDto);
        Customer customer = customerService.signIn(accountDto.getEmail(), accountDto.getPassword());
        log.info("*** Customer Signed-in: {} ***", customer);
        return "Welcome " + customer.getFirstName() + " " + customer.getLastName();
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @RequestBody AccountDto accountDto) {
        log.info("*** Change Password for: {} ***",accountDto);
        Customer customer = customerService.changePassword(accountDto);
        log.info("*** Password Changed for: {} ***",customer);
        return "Password Changed For " + customer.getFirstName() + " " + customer.getLastName();
    }

    @PostMapping("/increase-credit")
    public String increaseCredit(@Valid @RequestBody CreditDto creditDto) {
        log.info("*** Increase Credit for: {} ***",creditDto);
        customerService.increaseCredit(creditDto.getCustomerEmail(), creditDto.getAmount());
        log.info("*** Credit Increased for: {} ***",creditDto);
        return "Credit Increased ";
    }

    @GetMapping("/filter")
    public Iterable<CustomerDto> search(@RequestParam String search) {
        log.info("*** Search for: {} ***",search);
        Iterable<CustomerDto> customerDtos = UserMapper.INSTANCE.convertCustomerIterator(customerService.findAll(search));
        log.info("*** : Search Results: {} ***",customerDtos);
        return customerDtos;
    }

}
