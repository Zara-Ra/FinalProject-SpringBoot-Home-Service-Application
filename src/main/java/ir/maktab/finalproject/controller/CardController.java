package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.CardDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
@CrossOrigin
public class CardController {

    @PostMapping("/pay_online")
    public String payOnline(@ModelAttribute CardDto cardDto) {
        System.out.println(cardDto.getCardNumber());
        System.out.println(cardDto.getExpirationDate());
        System.out.println(cardDto.getCvv2());
        return "OK";
    }
}
