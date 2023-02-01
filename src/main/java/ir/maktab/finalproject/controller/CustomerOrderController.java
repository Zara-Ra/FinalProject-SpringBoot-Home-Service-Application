package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.service.impl.CustomerOrderService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping("/request")
    public String requestOrder(@RequestBody CustomerOrderDto customerOrderDto){
        customerOrderService.requestOrder(Mapper.INSTANCE.convertOrder(customerOrderDto));
        return "Order Has Been Registered";
    }

}
