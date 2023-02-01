package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.PhotoInfoDto;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.impl.ExpertService;
import ir.maktab.finalproject.service.impl.SubServiceService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;
    private final SubServiceService subServiceService;

    public ExpertController(ExpertService expertService, SubServiceService subServiceService) {
        this.expertService = expertService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/register")
    public String register(@RequestBody ExpertDto expertDto) {
        expertService.signUp(Mapper.INSTANCE.convertExpert(expertDto));
        return "Expert Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDto accountDto) {
        Expert expert = expertService.signIn(accountDto.getEmail(), accountDto.getPassword());
        return "Welcome " + expert.getFirstName() + " " + expert.getLastName();
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestBody AccountDto accountDto) {
        Expert expert = expertService.changePassword(accountDto.getEmail(), accountDto.getPassword(), accountDto.getNewPassword());
        return "Password Changed For " + expert.getFirstName() + " " + expert.getLastName();
    }

    @GetMapping("/new_experts")
    public List<ExpertDto> findNewExperts() {
        return Mapper.INSTANCE.convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.NEW));
    }

    @GetMapping("/update_status")
    public String updateStatus(@RequestParam Integer expertId) {
        expertService.setExpertStatus(expertId, ExpertStatus.APPROVED);
        return "Expert Status Updated";
    }

    @GetMapping("/assign_subservice")
    public String assignSubService(@RequestParam String subServiceName, @RequestParam String expertEmail) {
        Expert expert = expertService.findByEmail(expertEmail)
                .orElseThrow(() -> new NotExitsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExitsException("SubService Not Exits"));

        expertService.addSubServiceToExpert(subService, expert);
        return "Sub Service Assigned To Expert";
    }

    @GetMapping("/delete_subservice")
    public String deleteSubService(@RequestParam String subServiceName, @RequestParam String expertEmail) {
        Expert expert = expertService.findByEmail(expertEmail)
                .orElseThrow(() -> new NotExitsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExitsException("SubService Not Exits"));

        expertService.deleteSubServiceFromExpert(subService, expert);
        return "Sub Service Deleted From Expert";
    }

    @PostMapping("/save_photo")
    public String savePhoto(@RequestBody PhotoInfoDto photoInfoDto){
        expertService.getExpertPhoto(photoInfoDto.getOwnerEmail(), photoInfoDto.getSavePath());
        return "Photo Saved";
    }
}
