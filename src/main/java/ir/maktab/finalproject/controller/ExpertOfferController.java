package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.mapper.OfferMapper;
import ir.maktab.finalproject.service.impl.ExpertOfferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offer")
@Validated
public class ExpertOfferController {
    private final ExpertOfferService expertOfferService;

    public ExpertOfferController(ExpertOfferService expertOfferService) {
        this.expertOfferService = expertOfferService;
    }

    @PostMapping("/submit_offer")
    public String submitOffer(@Valid @RequestBody ExpertOfferDto expertOfferDto) {
        expertOfferService.submitOffer(expertOfferDto.getOrderId(), OfferMapper.INSTANCE.convertOffer(expertOfferDto));
        return "Offer Has Been Submitted";
    }

    @GetMapping("/chose_offer")
    public String choseOffer(@RequestParam @Min(1) Integer orderId, @RequestParam @Min(1) Integer offerId) {
        expertOfferService.choseOffer(orderId, offerId);
        return "Offer Has Been Chosen";
    }

    @GetMapping("/expert_arrived")
    public String expertArrived(@RequestParam @Min(1) Integer orderId, @RequestParam @Min(1) Integer offerId){
        expertOfferService.expertArrived(orderId,offerId);
        return "Expert Arrived";
    }

    @GetMapping("/expert_done")
    public String expertDone(@RequestParam @Min(1) Integer orderId, @RequestParam @Min(1) Integer offerId){
        expertOfferService.expertDone(orderId,offerId);
        return "Expert Done";
    }
    @GetMapping("/experts_orders/{expertEmail}")
    public List<ExpertOfferDto> findAcceptedOrdersFor(@PathVariable @Email String expertEmail){
        return OfferMapper.INSTANCE.convertOfferList(expertOfferService.findAcceptedOffersFor(expertEmail));
    }

}
