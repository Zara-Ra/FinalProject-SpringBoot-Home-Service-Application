package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.ExpertOfferRepository;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExpertOfferService {
    private final ExpertOfferRepository expertOfferRepository;
    private final CustomerOrderService customerOrderService;

    public ExpertOfferService(ExpertOfferRepository expertOfferRepository, CustomerOrderService customerOrderService) {
        this.expertOfferRepository = expertOfferRepository;
        this.customerOrderService = customerOrderService;
    }

    @Transactional
    public ExpertOffer submitOffer(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        if (!customerOrder.getSubService().equals(expertOffer.getSubService()))
            throw new OfferRequirementException("SubService Of Order and Offer Doesn't Match");
        if (expertOffer.getPrice() < expertOffer.getSubService().getBasePrice())
            throw new OfferRequirementException("Price Of Offer Should Be Greater Than Base Price Of The Sub-Service ( "
                    + expertOffer.getSubService().getSubName() + " " + expertOffer.getSubService().getBasePrice() + " )");
        if (expertOffer.getPreferredDate().before(new Date())
                || expertOffer.getPreferredDate().before(customerOrder.getPreferredDate()))
            throw new OfferRequirementException("The Preferred Date Should Be After Now And After The Customers Preferred Date");
        if (expertOffer.getDuration() == null)
            throw new OfferRequirementException("Duration Should Not Be Empty");

        if (customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)) {
            customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        }

        ExpertOffer saveOffer = expertOfferRepository.save(expertOffer);
        customerOrder.getExpertOfferList().add(expertOffer);
        customerOrderService.updateOrder(customerOrder);
        return saveOffer;
    }

    @Transactional
    public void choseOffer(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        if (!customerOrder.getExpertOfferList().contains(expertOffer))
            throw new NotExitsException("Offer Is Not For This Order");
        expertOffer.setIsChosen(true);
        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_ARRIVAL);
        customerOrder.setAcceptedExpertOffer(expertOffer);
        customerOrderService.updateOrder(customerOrder);
        expertOfferRepository.save(expertOffer);
    }

    public List<ExpertOffer> findAcceptedOrdersFor(Expert expert) {
        return expertOfferRepository.findAllByExpertAndIsChosen(expert, true);
    }

    public long countByIsChosen(boolean isChosen) {
        return expertOfferRepository.countByIsChosen(isChosen);
    }
}
