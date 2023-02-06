package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.CardDto;
import ir.maktab.finalproject.util.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
@CrossOrigin
public class CardController {

    @PostMapping("/pay_online")
    public String payOnline(@Valid @ModelAttribute("cardDto") CardDto cardDto,HttpServletRequest request) {
        System.out.println(cardDto.getCardNumber());
        System.out.println(cardDto.getCaptcha());
        System.out.println(request.getSession().getAttribute("captcha"));
        if (!cardDto.getCaptcha().equals(request.getSession().getAttribute("captcha")))
            throw new ValidationException("Captcha Mismatch");


        return "Pay Online";
    }

}
