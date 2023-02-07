package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.controller.enums.SortType;
import ir.maktab.finalproject.data.dto.AcceptedOrderDto;
import ir.maktab.finalproject.data.dto.PaymentDto;
import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.enums.PaymentType;
import ir.maktab.finalproject.data.mapper.OfferMapper;
import ir.maktab.finalproject.data.mapper.OrderMapper;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.impl.CustomerOrderService;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.sort.SortExpertOffer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
@Validated
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping("/request_order")
    public String requestOrder(@Valid @RequestBody CustomerOrderDto customerOrderDto) {
        customerOrderService.requestOrder(OrderMapper.INSTANCE.convertOrder(customerOrderDto));
        return "Order Has Been Registered";
    }

    @GetMapping("/all_orders_for_sub/{subName}")
    public List<CustomerOrderDto> findAllBySubService(@PathVariable String subName) {
        return OrderMapper.INSTANCE.convertOrderList(customerOrderService.findAllBySubServiceAndTwoStatus(subName));
    }

    @GetMapping("/all_offers_for_order")
    public List<ExpertOfferDto> getAllOffersForOrder(@RequestParam @Min(1) Integer orderId) {
        return OfferMapper.INSTANCE.convertOfferList
                (customerOrderService.getAllOffersForOrder(orderId, SortExpertOffer.SortByPriceAcs));
    }

    @GetMapping("/all_offers_for_order_sort")
    public List<ExpertOfferDto> getAllOffersForOrder(@RequestParam @Min(1) Integer orderId, @RequestParam String sortBy) {
        Comparator<ExpertOffer> comparator;
        try {
            comparator = SortType.valueOf(sortBy).getComparator();
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid Sort Type");
        }
        return OfferMapper.INSTANCE.convertOfferList
                (customerOrderService.getAllOffersForOrder(orderId, comparator));
    }

    @GetMapping("/find_order")
    public CustomerOrderDto findOrder(@RequestParam @Min(1) Integer orderId) {
        return OrderMapper.INSTANCE.convertOrder(customerOrderService.findById(orderId).orElseThrow(
                () -> new NotExistsException("Order Not Exists")
        ));
    }

    @GetMapping("/find_accepted_order")
    public AcceptedOrderDto findAcceptedOrder(@RequestParam @Min(1) Integer orderId) {
        return OrderMapper.INSTANCE.convertAcceptedOrder(customerOrderService.findById(orderId).orElseThrow(
                () -> new NotExistsException("Order Not Exists")
        ));
    }

    @CrossOrigin
    @PostMapping("/pay_online")
    public String payOnline(@Valid @ModelAttribute PaymentDto paymentDto, HttpServletRequest request) {
        try {
            Date expirationDate = new SimpleDateFormat("yyyy-MM").parse(paymentDto.getExpirationDate());
            if(expirationDate.before(new Date())){
                throw new ValidationException("Card Has Expired");}
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date Format");
        }
        if (!paymentDto.getCaptcha().equals(request.getSession().getAttribute("captcha")))
            throw new ValidationException("Captcha Mismatch");
        //call bank
        customerOrderService.pay(paymentDto.getOrderId(), PaymentType.ONLINE);
        return "Order Payed Online";
    }

    @GetMapping("/pay_credit")
    public String payFromCredit(@RequestParam @Min(1) Integer orderId){
        customerOrderService.pay(orderId,PaymentType.CREDIT);
        return "Order Payed By Credit";
    }
}
