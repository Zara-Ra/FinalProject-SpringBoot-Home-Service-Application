package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.service.impl.CustomerService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String register(@RequestBody CustomerDto customerDto) {
        customerService.signUp(Mapper.INSTANCE.convertCustomer(customerDto));
        return "Customer Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDto accountDto){
        Customer customer = customerService.signIn(accountDto.getEmail(), accountDto.getPassword());
        return "Welcome "+ customer.getFirstName()+" "+customer.getLastName();
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestBody AccountDto accountDto){
        Customer customer = customerService.changePassword(accountDto.getEmail(), accountDto.getPassword(),accountDto.getNewPassword());
        return "Password Changed For "+ customer.getFirstName()+" "+customer.getLastName();
    }

}
