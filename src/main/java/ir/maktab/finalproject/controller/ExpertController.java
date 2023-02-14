package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.PhotoInfoDto;
import ir.maktab.finalproject.data.dto.ReviewDto;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.mapper.ReviewMapper;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.impl.ExpertService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expert")
@Validated
public class ExpertController {
    private final ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody ExpertDto expertDto) {
        expertService.signUp(UserMapper.INSTANCE.convertExpert(expertDto));
        return "Expert Registered";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AccountDto accountDto) {
        Expert expert = expertService.signIn(accountDto.getEmail(), accountDto.getPassword());
        return "Welcome " + expert.getFirstName() + " " + expert.getLastName();
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @RequestBody AccountDto accountDto) {
        Expert expert = expertService.changePassword(accountDto);
        return "Password Changed For " + expert.getFirstName() + " " + expert.getLastName();
    }

    @GetMapping("/new-experts")
    public List<ExpertDto> findNewExperts() {
        return UserMapper.INSTANCE.convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.NEW));
    }

    @GetMapping("/approve-status")
    public String approveStatus(@RequestParam @Min(1) Integer expertId) {
        expertService.setExpertStatus(expertId, ExpertStatus.APPROVED);
        return "Expert Status Updated";
    }

    @GetMapping("/assign-sub-service")
    public String assignSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        expertService.addSubServiceToExpert(subServiceName, expertEmail);
        return "Sub Service Assigned To Expert";
    }

    @GetMapping("/delete-sub-service")
    public String deleteSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        expertService.deleteSubServiceFromExpert(subServiceName, expertEmail);
        return "Sub Service Deleted From Expert";
    }

    @PostMapping("/save-photo")
    public String savePhoto(@Valid @RequestBody PhotoInfoDto photoInfoDto) {
        expertService.getExpertPhoto(photoInfoDto.getOwnerEmail(), photoInfoDto.getSavePath());
        return "Photo Saved";
    }

    @GetMapping("/order-score")
    public ReviewDto orderScore(@RequestParam Integer orderId, @RequestParam String expertEmail) {
        return ReviewMapper.INSTANCE.convertReview(expertService.getOrderScore(orderId, expertEmail));
    }

    @GetMapping("/filter")
    public Iterable<ExpertDto> search(@RequestParam String search) {
        return UserMapper.INSTANCE.convertExpertIterator(expertService.findAll(search));
    }
}
