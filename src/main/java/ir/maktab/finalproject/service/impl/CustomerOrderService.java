package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.CustomerOrderRepository;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.util.sort.SortExpertOffer;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerService customerService;
    private final SubServiceService subServiceService;
    public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerService customerService, SubServiceService subServiceService) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerService = customerService;
        this.subServiceService = subServiceService;
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
                .orElseThrow(()->new UserNotFoundException("Customer Not Exits"));

        SubService subService = subServiceService.findByName(customerOrder.getSubService().getSubName())
                .orElseThrow(()-> new NotExitsException("Sub Serviec Not Exits"));

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

    public List<CustomerOrder> findAllBySubServiceAndTwoStatus(SubService subService) {
        return customerOrderRepository.findAllBySubServiceAndTwoStatus(subService, OrderStatus.WAITING_FOR_EXPERT_SELECTION
                , OrderStatus.WAITING_FOR_EXPERT_OFFER);
    }

    public CustomerOrder updateOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public List<ExpertOffer> getAllOffersForOrder(CustomerOrder customerOrder) {
        CustomerOrder foundOrder = customerOrderRepository.findById(customerOrder.getId())
                .orElseThrow(() -> new NotExitsException("Customer Order Not Found"));
        foundOrder.getExpertOfferList().sort(SortExpertOffer.SortByPriceAcs);
        return foundOrder.getExpertOfferList();
    }

    public List<ExpertOffer> getAllOffersForOrder(CustomerOrder customerOrder, Comparator<ExpertOffer> comparator) {
        customerOrder.getExpertOfferList().sort(comparator);
        return customerOrder.getExpertOfferList();
    }

    public Optional<CustomerOrder> getOrderForAcceptedOffer(ExpertOffer expertOffer) {
        return customerOrderRepository.findByAcceptedExpertOffer(expertOffer);
    }

    public CustomerOrder expertArrived(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        Date now = new Date();
        if (expertOffer.getPreferredDate().after(now))
            throw new OfferRequirementException("Expert Can't Start Work Before His/Her PreferredDate");
        customerOrder.setStartDate(now);
        customerOrder.setStatus(OrderStatus.STARTED);
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder expertDone(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        customerOrder.setFinishDate(new Date());
        customerOrder.setStatus(OrderStatus.DONE);
        return customerOrderRepository.save(customerOrder);

        // todo calculate duration(next phase) we will need ExpertOffer here
    }
}
