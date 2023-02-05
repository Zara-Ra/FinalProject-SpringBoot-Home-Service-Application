package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.PhotoInfoDto;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.impl.ExpertService;
import ir.maktab.finalproject.service.impl.SubServiceService;
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
    private final SubServiceService subServiceService;

    public ExpertController(ExpertService expertService, SubServiceService subServiceService) {
        this.expertService = expertService;
        this.subServiceService = subServiceService;
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

    @PostMapping("/change_password")
    public String changePassword(@Valid @RequestBody AccountDto accountDto) {
        Expert expert = expertService.changePassword(accountDto);
        return "Password Changed For " + expert.getFirstName() + " " + expert.getLastName();
    }

    @GetMapping("/new_experts")
    public List<ExpertDto> findNewExperts() {
        return UserMapper.INSTANCE.convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.NEW));
    }

    @GetMapping("/update_status")
    public String updateStatus(@RequestParam @Min(1) Integer expertId) {
        expertService.setExpertStatus(expertId, ExpertStatus.APPROVED);
        return "Expert Status Updated";
    }

    @GetMapping("/assign_subservice")
    public String assignSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        Expert expert = expertService.findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException("SubService Not Exits"));

        expertService.addSubServiceToExpert(subService, expert);
        return "Sub Service Assigned To Expert";
    }

    @GetMapping("/delete_subservice")
    public String deleteSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        Expert expert = expertService.findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException("SubService Not Exits"));

        expertService.deleteSubServiceFromExpert(subService, expert);
        return "Sub Service Deleted From Expert";
    }

    @PostMapping("/save_photo")
    public String savePhoto(@Valid @RequestBody PhotoInfoDto photoInfoDto) {
        expertService.getExpertPhoto(photoInfoDto.getOwnerEmail(), photoInfoDto.getSavePath());
        return "Photo Saved";
    }
}
