package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.CustomerOrderRepository;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final CustomerOrderRepository customerOrderRepository;

    public void requestOrder(Customer customer, CustomerOrder customerOrder) {
        if (customerOrder.getPrice() < customerOrder.getSubService().getBasePrice())
            throw new OrderRequirementException("Price Of Order Should Be Greater Than Base Price Of The Sub-Service:( "
                    + customerOrder.getSubService().getSubName() + " " + customerOrder.getSubService().getBasePrice() + " )");

        if (customerOrder.getPreferredDate().before(new Date()))
            throw new OrderRequirementException("The Preferred Date Should Be After Now");

        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
        customer.getCustomerOrderList().add(customerOrder);
        customerOrderRepository.save(customerOrder);
    }
    public List<CustomerOrder> findAllBySubServiceAndStatus(SubService subService) {
        return customerOrderRepository.findAllBySubServiceAndStatus(subService, OrderStatus.WAITING_FOR_EXPERT_SELECTION
                , OrderStatus.WAITING_FOR_EXPERT_OFFER);
    }

    public void updateOrder(CustomerOrder customerOrder) {
        customerOrderRepository.save(customerOrder);
    }
}
