package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.service.impl.CustomerService;
import ir.maktab.finalproject.service.impl.ExpertService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PostMapping("/register")
    public String register(@RequestBody ExpertDto expertDto) {
        expertService.signUp(Mapper.INSTANCE.convertExpert(expertDto));
        return "Expert Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDto accountDto){
        Expert ex = expertService.signIn(accountDto.getEmail(), accountDto.getPassword());
        return "Welcome "+ ex.getFirstName();
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestBody AccountDto accountDto){
        Expert expert = expertService.changePassword(accountDto.getEmail(), accountDto.getPassword(),accountDto.getNewPassword());
        return "Password Changed For "+ expert.getFirstName();
    }

}
