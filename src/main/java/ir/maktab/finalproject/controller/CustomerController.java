package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AcceptedOrderDto;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.CreditDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.mapper.OrderMapper;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.impl.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        Customer customer = customerService.register(UserMapper.INSTANCE.convertCustomer(customerDto)
                ,"");
        log.info("*** New Customer Added : {} ***", customer);
        return "Customer Registered Successfully";
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String changePassword(@Valid @RequestBody AccountDto accountDto, Principal principal) {
        log.info("*** Change Password for: {} ***", accountDto);
        accountDto.setEmail(principal.getName());
        Customer customer = customerService.changePassword(accountDto);
        log.info("*** Password Changed for: {} ***", customer);
        return "Password Changed For " + customer.getFirstName() + " " + customer.getLastName();
    }

    @PostMapping("/increase-credit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String increaseCredit(@Valid @RequestBody CreditDto creditDto, Principal principal) {
        log.info("*** Increase Credit for: {} ***", creditDto);
        creditDto.setCustomerEmail(principal.getName());
        customerService.increaseCredit(creditDto.getCustomerEmail(), creditDto.getAmount());
        log.info("*** Credit Increased for: {} ***", creditDto);
        return "Credit Increased ";
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<CustomerDto> search(@RequestParam String search) {
        log.info("*** Search for: {} ***", search);
        Iterable<CustomerDto> customerDtos = UserMapper.INSTANCE.convertCustomerIterator(customerService.findAll(search));
        log.info("*** : Search Results: {} ***", customerDtos);
        return customerDtos;
    }

    @GetMapping("/credit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public double showCredit(Principal principal){
        log.info("*** Show Credit For {} ***",principal.getName());
        double credit = customerService.getCredit(principal.getName());
        log.info("*** Credit For {}: {} ***",principal.getName(),credit);
        return credit;
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<AcceptedOrderDto> showAllOrders(Principal principal){
        log.info("*** Show Orders For {} ***",principal.getName());
        List<AcceptedOrderDto> orderDtoList = OrderMapper.INSTANCE.convertAcceptedOrderList(
                customerService.getAllOrders(principal.getName()));
        log.info("*** Orders For {}: {} ***",principal.getName(),orderDtoList);
        return orderDtoList;
    }
}
