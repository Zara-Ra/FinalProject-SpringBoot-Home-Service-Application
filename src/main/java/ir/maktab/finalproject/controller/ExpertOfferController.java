package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.mapper.Mapper;
import ir.maktab.finalproject.service.impl.ExpertOfferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/offer")
public class ExpertOfferController {
    private final ExpertOfferService expertOfferService;

    public ExpertOfferController(ExpertOfferService expertOfferService) {
        this.expertOfferService = expertOfferService;
    }

    @PostMapping("/submit_offer")
    public String submitOffer(@RequestBody ExpertOfferDto expertOfferDto) {
        expertOfferService.submitOffer(expertOfferDto.getOrderId(), Mapper.INSTANCE.convertOffer(expertOfferDto));

        return "Offer Has Been Submitted";
    }

}
