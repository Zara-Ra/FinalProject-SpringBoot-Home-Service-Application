package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.CustomerOrderRepository;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;

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
    public List<CustomerOrder> findAllBySubServiceAndStatus(SubService subService) {
        return customerOrderRepository.findAllBySubServiceAndStatus(subService, OrderStatus.WAITING_FOR_EXPERT_SELECTION
                , OrderStatus.WAITING_FOR_EXPERT_OFFER);
    }

    public void updateOrder(CustomerOrder customerOrder) {
        customerOrderRepository.save(customerOrder);
    }

    public List<ExpertOffer> getAllOffersForOrder(CustomerOrder customerOrder, Comparator<ExpertOffer> comparator){
        CustomerOrder foundOrder = customerOrderRepository.findById(customerOrder.getId())
                .orElseThrow(() -> new NotExitsException("Customer Order Not Found"));
        foundOrder.getExpertOfferList().sort(comparator);
        return foundOrder.getExpertOfferList();
    }

    public CustomerOrder getOrderForAcceptedOffer(ExpertOffer expertOffer){
        return customerOrderRepository.findByAcceptedExpertOffer(expertOffer);
    }
    public void expertArrived(CustomerOrder customerOrder, ExpertOffer expertOffer){
        if(expertOffer.getPreferredDate().after(new Date()))
            throw new OfferRequirementException("Expert Can't Start Work Before His/Her PreferredDate");
        customerOrder.setStatus(OrderStatus.STARTED);
        customerOrderRepository.save(customerOrder);
    }

    public void expertDone(CustomerOrder customerOrder, ExpertOffer expertOffer){
        customerOrder.setStatus(OrderStatus.DONE);
        customerOrderRepository.save(customerOrder);

        //todo calculate duration(next phase) we will need ExpertOffer here
        // should also add two extra Date fields in CustomerOrder
        // to calculate the actual Duration and compare it with the experts claim
    }
}
