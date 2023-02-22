package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.maktab.finalproject.data.dto.PaymentUserDto;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.Review;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.data.enums.PaymentType;
import ir.maktab.finalproject.repository.CustomerOrderRepository;
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.service.predicates.order.OrderPredicateBuilder;
import ir.maktab.finalproject.service.predicates.user.UserPredicateBuilder;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerOrderService extends MainService {
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
            throw new OrderRequirementException(messageSource.getMessage("errors.message.invalid_price"));

        if (customerOrder.getPreferredDate().before(new Date()))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.invalid_preferred_date"));

        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
        customer.getCustomerOrderList().add(customerOrder);
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder requestOrder(CustomerOrder customerOrder) {

        Customer customer = customerService.findByEmail(customerOrder.getCustomer().getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.customer_not_exists")));

        SubService subService = subServiceService.findByName(customerOrder.getSubService().getSubName())
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.sub_not_exists")));

        if (customerOrder.getPrice() < customerOrder.getSubService().getBasePrice())
            throw new OrderRequirementException(messageSource.getMessage("errors.message.invalid_price"));

        if (customerOrder.getPreferredDate().before(new Date()))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.invalid_preferred_date"));

        customer.getCustomerOrderList().add(customerOrder);
        customerOrder.setCustomer(customer);
        customerOrder.setSubService(subService);
        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
        return customerOrderRepository.save(customerOrder);
    }

    @Transactional
    public List<CustomerOrder> findAllBySubServiceAndTwoStatus(String subServiceName) {
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.sub_not_exists")));
        return customerOrderRepository.findAllBySubServiceAndTwoStatus(subService, OrderStatus.WAITING_FOR_EXPERT_SELECTION
                , OrderStatus.WAITING_FOR_EXPERT_OFFER);
    }

    public CustomerOrder updateOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public List<ExpertOffer> getAllOffersForOrder(Integer customerOrderId, Comparator<ExpertOffer> comparator) {
        CustomerOrder foundOrder = customerOrderRepository.findById(customerOrderId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));
        foundOrder.getExpertOfferList().sort(comparator);
        return foundOrder.getExpertOfferList();
    }

    public Optional<CustomerOrder> findById(Integer orderId) {
        return customerOrderRepository.findById(orderId);
    }

    @Transactional
    public void pay(PaymentUserDto paymentDto) {
        CustomerOrder customerOrder = customerOrderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        if (!customerOrder.getStatus().equals(OrderStatus.DONE))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_not_done"));

        double payAmount = customerOrder.getAcceptedExpertOffer().getPrice();

        if (paymentDto.getPaymentType().equals(PaymentType.CREDIT)) {
            Customer customer = customerOrder.getCustomer();
            customerService.pay(customer, payAmount);
        }

        Expert expert = customerOrder.getAcceptedExpertOffer().getExpert();
        expertService.pay(expert, payAmount);

        customerOrder.setStatus(OrderStatus.PAYED);
        customerOrderRepository.save(customerOrder);
    }

    @Transactional
    public void addReview(Review review) {
        CustomerOrder customerOrder = customerOrderRepository.findById(review.getCustomerOrder().getId())
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        if(!review.getCustomerOrder().getCustomer().getEmail().equals(customerOrder.getCustomer().getEmail()))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_customer_mismatch"));

        if (!customerOrder.getStatus().equals(OrderStatus.PAYED))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_not_payed"));

        Expert expert = customerOrder.getAcceptedExpertOffer().getExpert();
        review.setCustomerOrder(customerOrder);
        expert.getReviewList().add(review);
        double averageScore = expert.getReviewList().stream().mapToInt(Review::getScore).average().getAsDouble();
        expert.setAverageScore(averageScore);
        customerOrder.setStatus(OrderStatus.SCORED);
        customerOrderRepository.save(customerOrder);
        expertService.updateExpert(expert);
    }

    @Transactional
    public Iterable<CustomerOrder> findAll(String search) {
        if (search.isEmpty())
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_null_search"));

        OrderPredicateBuilder builder = new OrderPredicateBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        BooleanExpression expression = builder.build();
        return customerOrderRepository.findAll(expression);
    }

}
