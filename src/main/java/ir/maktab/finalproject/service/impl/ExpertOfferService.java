package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.repository.ExpertOfferRepository;
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class ExpertOfferService extends MainService {
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
    public ExpertOffer submitOffer(Integer orderId, ExpertOffer expertOffer) {
        CustomerOrder customerOrder = customerOrderService.findById(orderId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        SubService offerSubService = subServiceService.findByName(expertOffer.getSubService().getSubName())
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.sub_not_exists")));

        Expert expert = expertService.findByEmail(expertOffer.getExpert().getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));

        if (!customerOrder.getSubService().equals(offerSubService))
            throw new OfferRequirementException(messageSource.getMessage("errors.message.sub_order_offer_mismatch"));

        if (expertOffer.getPrice() < offerSubService.getBasePrice())
            throw new OfferRequirementException(messageSource.getMessage("errors.message.invalid_price"));

        if (expertOffer.getPreferredDate().before(new Date())
                || expertOffer.getPreferredDate().before(customerOrder.getPreferredDate()))
            throw new OfferRequirementException(messageSource.getMessage("errors.message.invalid_preferred_date_Offer"));

        if (expertOffer.getDuration() == null)
            throw new OfferRequirementException(messageSource.getMessage("errors.message.invalid_duration"));

        if (customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)) {
            customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        }

        expertOffer.setCustomerOrder(customerOrder);
        expertOffer.setExpert(expert);
        expertOffer.setSubService(offerSubService);
        expertOffer.setIsChosen(false);
        ExpertOffer saveOffer = expertOfferRepository.save(expertOffer);

        customerOrder.getExpertOfferList().add(saveOffer);
        customerOrderService.updateOrder(customerOrder);
        return saveOffer;
    }

    @Transactional
    public void choseOffer(Integer orderId, Integer offerId) {
        CustomerOrder customerOrder = customerOrderService.findById(orderId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        ExpertOffer expertOffer = expertOfferRepository.findById(offerId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.offer_not_exists")));

        if (!(customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)
                || customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SELECTION)))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_status_mismatch"));

        if (!customerOrder.getExpertOfferList().contains(expertOffer))
            throw new NotExistsException(messageSource.getMessage("errors.message.offer_order_mismatch"));

        expertOffer.setIsChosen(true);
        customerOrder.setStatus(OrderStatus.WAITING_FOR_EXPERT_ARRIVAL);
        customerOrder.setAcceptedExpertOffer(expertOffer);
        customerOrderService.updateOrder(customerOrder);
        expertOfferRepository.save(expertOffer);
    }

    public CustomerOrder expertArrived(Integer orderId, Integer offerId) {
        CustomerOrder customerOrder = customerOrderService.findById(orderId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        ExpertOffer expertOffer = expertOfferRepository.findById(offerId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.offer_not_exists")));

        if (!customerOrder.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_ARRIVAL))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_status_not_expert_arrival"));

        Date now = new Date();
        if (expertOffer.getPreferredDate().after(now))
            throw new OfferRequirementException(messageSource.getMessage("errors.message.invalid_expert_start"));
        customerOrder.setStartDate(now);
        customerOrder.setStatus(OrderStatus.STARTED);
        return customerOrderService.updateOrder(customerOrder);
    }

    @Transactional
    public CustomerOrder expertDone(Integer orderId, Integer offerId) {
        CustomerOrder customerOrder = customerOrderService.findById(orderId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.order_not_exists")));

        ExpertOffer expertOffer = expertOfferRepository.findById(offerId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.offer_not_exists")));

        if (!customerOrder.getStatus().equals(OrderStatus.STARTED))
            throw new OrderRequirementException(messageSource.getMessage("errors.message.order_not_started"));

        customerOrder.setFinishDate(new Date());
        customerOrder.setStatus(OrderStatus.DONE);
        calculateExpertDelay(customerOrder, expertOffer);
        return customerOrderService.updateOrder(customerOrder);
    }

    private void calculateExpertDelay(CustomerOrder customerOrder, ExpertOffer expertOffer) {
        Date start = customerOrder.getStartDate();
        Date finish = customerOrder.getFinishDate();

        Duration duration = Duration.between(start.toInstant(), finish.toInstant());

        long hours = expertOffer.getDuration().minus(duration).toHours();
        if (hours >= 0)
            return;

        double averageScore = expertOffer.getExpert().getAverageScore() - Math.abs(hours);
        expertOffer.getExpert().setAverageScore(averageScore);
        if (averageScore < 0)
            expertOffer.getExpert().setStatus(ExpertStatus.SUSPEND);

        expertService.updateExpert(expertOffer.getExpert());
    }

    @Transactional
    public List<ExpertOffer> findAcceptedOffersFor(String expertEmail) {
        return expertOfferRepository.findAllByExpertEmailAndIsChosen(expertEmail, true);
    }

    public long countByIsChosen(boolean isChosen) {
        return expertOfferRepository.countByIsChosen(isChosen);
    }

}
