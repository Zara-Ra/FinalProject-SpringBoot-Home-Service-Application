package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.ExpertOfferRepository;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExpertOfferService {
    private final ExpertOfferRepository expertOfferRepository;

    private final CustomerOrderService customerOrderService;

    private final SubServiceService subServiceService;

    private final ExpertService expertService;

    public ExpertOfferService(ExpertOfferRepository expertOfferRepository, CustomerOrderService customerOrderService, SubServiceService subServiceService, ExpertService expertService) {
        this.expertOfferRepository = expertOfferRepository;
        this.customerOrderService = customerOrderService;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
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
    public ExpertOffer submitOffer(Integer orderId, ExpertOffer expertOffer) {
        CustomerOrder customerOrder = customerOrderService.findById(orderId)
                .orElseThrow(()->new NotExistsException("Order Not Exists"));

        SubService offerSubService = subServiceService.findByName(expertOffer.getSubService().getSubName())
                .orElseThrow(()->new NotExistsException("Sub Service Not Exists"));

        Expert expert = expertService.findByEmail(expertOffer.getExpert().getEmail())
                .orElseThrow(()-> new UserNotFoundException("Expert Not Exists"));

        if (!customerOrder.getSubService().equals(offerSubService))
            throw new OfferRequirementException("SubService Of Order and Offer Doesn't Match");

        if (expertOffer.getPrice() < offerSubService.getBasePrice())
            throw new OfferRequirementException("Price Of Offer Should Be Greater Than Base Price Of The Sub-Service");

        if (expertOffer.getPreferredDate().before(new Date())
                || expertOffer.getPreferredDate().before(customerOrder.getPreferredDate()))
            throw new OfferRequirementException("The Preferred Date Should Be After Now And After The Customers Preferred Date");

        if (expertOffer.getDuration() == null)
            throw new OfferRequirementException("Duration Should Not Be Empty");

        if (customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)) {
            customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        }

        expertOffer.setExpert(expert);
        expertOffer.setSubService(offerSubService);
        expertOffer.setIsChosen(false);
        ExpertOffer saveOffer = expertOfferRepository.save(expertOffer);

        customerOrder.getExpertOfferList().add(saveOffer);
        customerOrderService.updateOrder(customerOrder);
        return saveOffer;
    }

    @Transactional
    public void choseOffer(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        if (!customerOrder.getExpertOfferList().contains(expertOffer))
            throw new NotExistsException("Offer Is Not For This Order");
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
