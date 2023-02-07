package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.Review;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.data.enums.PaymentType;
import ir.maktab.finalproject.repository.CustomerOrderRepository;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerService customerService;

    private final SubServiceService subServiceService;

    private final ExpertService expertService;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerService customerService, SubServiceService subServiceService, ExpertService expertService) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerService = customerService;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
    }

    public CustomerOrder requestOrder(Customer customer, CustomerOrder customerOrder) {
        if (customerOrder.getPrice() < customerOrder.getSubService().getBasePrice())
            throw new OrderRequirementException("Price Of Order Should Be Greater Than Base Price Of The Sub-Service:( "
                    + customerOrder.getSubService().getSubName() + " " + customerOrder.getSubService().getBasePrice() + " )");

        if (customerOrder.getPreferredDate().before(new Date()))
            throw new OrderRequirementException("The Preferred Date Should Be After Now");

        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
        customer.getCustomerOrderList().add(customerOrder);
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder requestOrder(CustomerOrder customerOrder) {

        Customer customer = customerService.findByEmail(customerOrder.getCustomer().getEmail())
                .orElseThrow(() -> new UserNotFoundException("Customer Not Exits"));

        SubService subService = subServiceService.findByName(customerOrder.getSubService().getSubName())
                .orElseThrow(() -> new NotExistsException("Sub Service Not Exits"));

        if (customerOrder.getPrice() < customerOrder.getSubService().getBasePrice())
            throw new OrderRequirementException("Price Of Order Should Be Greater Than Base Price Of The Sub-Service:( "
                    + customerOrder.getSubService().getSubName() + " " + customerOrder.getSubService().getBasePrice() + " )");

        if (customerOrder.getPreferredDate().before(new Date()))
            throw new OrderRequirementException("The Preferred Date Should Be After Now");

        customer.getCustomerOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);
        customerOrder.setSubService(subService);
        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
        return customerOrderRepository.save(customerOrder);
    }

    public List<CustomerOrder> findAllBySubServiceAndTwoStatus(String subServiceName) {
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException("Sub Service Not Exits"));
        return customerOrderRepository.findAllBySubServiceAndTwoStatus(subService, OrderStatus.WAITING_FOR_EXPERT_SELECTION
                , OrderStatus.WAITING_FOR_EXPERT_OFFER);
    }

    public CustomerOrder updateOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public List<ExpertOffer> getAllOffersForOrder(Integer customerOrderId, Comparator<ExpertOffer> comparator) {
        CustomerOrder foundOrder = customerOrderRepository.findById(customerOrderId)
                .orElseThrow(() -> new NotExistsException("Customer Order Not Found"));
        foundOrder.getExpertOfferList().sort(comparator);
        return foundOrder.getExpertOfferList();
    }

    public Optional<CustomerOrder> getOrderForAcceptedOffer(ExpertOffer expertOffer) {//todo not used
        return customerOrderRepository.findByAcceptedExpertOffer(expertOffer);
    }

    public Optional<CustomerOrder> findById(Integer orderId) {
        return customerOrderRepository.findById(orderId);
    }

    @Transactional
    public void pay(Integer orderId, PaymentType paymentType) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotExistsException("Order Not Exists"));
        double payAmount = customerOrder.getAcceptedExpertOffer().getPrice();

        if(paymentType.equals(PaymentType.CREDIT)) {
            Customer customer = customerOrder.getCustomer();
            customerService.pay(customer, payAmount);
        }

        Expert expert = customerOrder.getAcceptedExpertOffer().getExpert();
        expertService.pay(expert,payAmount);

        customerOrder.setStatus(OrderStatus.PAYED);
        customerOrderRepository.save(customerOrder);
    }

    @Transactional
    public void addReview(Review review) {
        CustomerOrder customerOrder = customerOrderRepository.findById(review.getCustomerOrder().getId())
                .orElseThrow(() -> new NotExistsException("Order Not Exists"));
        if(!customerOrder.getStatus().equals(OrderStatus.PAYED))
            throw new OrderRequirementException("Order Not Payed Yet");

        Expert expert = customerOrder.getAcceptedExpertOffer().getExpert();
        review.setCustomerOrder(customerOrder);
        expert.getReviewList().add(review);
        double averageScore = expert.getReviewList().stream().mapToInt(r->r.getScore()).average().getAsDouble();
        expert.setAverageScore(averageScore);
        expertService.updateExpert(expert);
    }
}
