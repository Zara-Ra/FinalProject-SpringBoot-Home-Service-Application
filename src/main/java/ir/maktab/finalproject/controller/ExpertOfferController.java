package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.service.impl.ExpertOfferService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class ExpertOfferController {
    private final ExpertOfferService expertOfferService;

    public ExpertOfferController(ExpertOfferService expertOfferService) {
        this.expertOfferService = expertOfferService;
    }

    /*@PostMapping("/request")
    public String submitOffer(@RequestBody ExpertOfferDto expertOfferDto){
        expertOfferService.submitOffer()
        return "Offer Has Been Registered";
    }*/

}
