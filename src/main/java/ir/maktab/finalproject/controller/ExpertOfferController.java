package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.mapper.OfferMapper;
import ir.maktab.finalproject.service.impl.ExpertOfferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offer")
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

}
