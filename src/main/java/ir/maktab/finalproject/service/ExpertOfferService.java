package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.ExpertOfferRepository;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertOfferService {
    private final ExpertOfferRepository expertOfferRepository;
    private final CustomerOrderService customerOrderService;

    public ExpertOffer submitOffer(ExpertOffer expertOffer){
        if(expertOffer.getPrice() < expertOffer.getSubService().getBasePrice())
            throw new OfferRequirementException("Price Of Offer Should Be Greater Than Base Price Of The Sub-Service"
                    + expertOffer.getSubService().getSubName()+ " " + expertOffer.getSubService().getBasePrice() );
        if(expertOffer.getPreferredDate().before(new Date())
                || expertOffer.getPreferredDate().before(expertOffer.getCustomerOrder().getPreferredDate()))
            throw new OfferRequirementException("The Preferred Date Should Be After Now And After The Customers Preferred Date");
        if(expertOffer.getDuration() == null)
            throw new OfferRequirementException("Duration Should Not Be Empty");

        if(expertOffer.getCustomerOrder().getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)) {
            expertOffer.getCustomerOrder().setStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
            customerOrderService.updateOrder(expertOffer.getCustomerOrder());
        }
        return expertOfferRepository.save(expertOffer);
    }

    public List<ExpertOffer> findAllExpertOfferFor(CustomerOrder customerOrder){
        return expertOfferRepository.findAllByCustomerOrderOrderByPriceAsc(customerOrder);//todo maybe incorrect name OrderBy
        //todo use Sort or OrderBy ??
    }

    public List<ExpertOffer> sortByPrice(List<ExpertOffer> expertOfferList){
        return null;
    }

    public List<ExpertOffer> sortByExpertAverageScore(List<ExpertOffer> expertOfferList){
        return null;
    }

    public void update(ExpertOffer expertOffer) {
        expertOfferRepository.save(expertOffer);
    }
}
