package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.service.impl.CustomerOrderService;
import ir.maktab.finalproject.data.mapper.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping("/request_order")
    public String requestOrder(@RequestBody CustomerOrderDto customerOrderDto) {
        customerOrderService.requestOrder(Mapper.INSTANCE.convertOrder(customerOrderDto));
        return "Order Has Been Registered";
    }

    @GetMapping("/all_order/{subName}")
    public List<CustomerOrderDto> findAllBySubService(@PathVariable String subName) {
        return Mapper.INSTANCE.convertOrderList(customerOrderService.findAllBySubServiceAndTwoStatus(subName));
    }

}
