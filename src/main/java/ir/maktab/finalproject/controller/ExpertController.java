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
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/expert")
@Validated
public class ExpertController {
    private final ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PostMapping("/register")
    //@PermitAll
    public String register(@Valid @RequestBody ExpertDto expertDto) {
        log.info("*** Add New Expert: {} ***", expertDto);
        Expert expert = expertService.register(UserMapper.INSTANCE.convertExpert(expertDto));
        log.info("*** New Expert Added : {} ***", expert);
        return "Expert Registered";
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('EXPERT')")
    public String changePassword(@Valid @RequestBody AccountDto accountDto) {
        log.info("*** Change Password for: {} ***", accountDto);
        Expert expert = expertService.changePassword(accountDto);
        log.info("*** Password Changed for: {} ***", expert);
        return "Password Changed For " + expert.getFirstName() + " " + expert.getLastName();
    }

    @GetMapping("/new-experts")
    public List<ExpertDto> findNewExperts() {
        log.info("*** Find New Experts ***");
        List<ExpertDto> expertDtos = UserMapper.INSTANCE
                .convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.NEW));
        log.info("*** Found Experts With NEW Status: {} ***", expertDtos);
        return expertDtos;
    }

    @GetMapping("/approve-status")
    public String approveStatus(@RequestParam @Min(1) Integer expertId) {
        log.info("*** Approve New Expert: {} ***", expertId);
        expertService.setExpertStatus(expertId, ExpertStatus.APPROVED);
        log.info("*** Expert Status Changed To APPROVED ***");
        return "Expert Status Updated";
    }

    @GetMapping("/assign-sub-service")
    public String assignSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        log.info("*** Assign Sub Service: {},To Expert: {} ***", subServiceName, expertEmail);
        expertService.addSubServiceToExpert(subServiceName, expertEmail);
        log.info("*** Sub Service: {} Assigned To Expert: {} ***", subServiceName, expertEmail);
        return "Sub Service Assigned To Expert";
    }

    @GetMapping("/delete-sub-service")
    public String deleteSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        log.info("*** Delete Sub Service: {},From Expert: {} ***", subServiceName, expertEmail);
        expertService.deleteSubServiceFromExpert(subServiceName, expertEmail);
        log.info("*** Sub Service: {} Deleted From Expert: {} ***", subServiceName, expertEmail);
        return "Sub Service Deleted From Expert";
    }

    @PostMapping("/save-photo")
    public String savePhoto(@Valid @RequestBody PhotoInfoDto photoInfoDto) {
        log.info("*** Save Expert Photo: {} ***", photoInfoDto);
        expertService.getExpertPhoto(photoInfoDto.getOwnerEmail(), photoInfoDto.getSavePath());
        log.info("*** Expert Photo Saved: {} ***", photoInfoDto);
        return "Photo Saved";
    }

    @GetMapping("/order-score")
    public ReviewDto orderScore(@RequestParam Integer orderId, @RequestParam String expertEmail) {
        log.info("*** Show Score For Expert: {}, Order: {} ***", expertEmail, orderId);
        ReviewDto reviewDto = ReviewMapper.INSTANCE.convertReview(expertService.getOrderScore(orderId, expertEmail));
        log.info("*** Score For Expert {}, Order: {} is {} ***", expertEmail, orderId, reviewDto);
        return reviewDto;
    }

    @GetMapping("/filter")
    public Iterable<ExpertDto> search(@RequestParam String search) {
        log.info("*** Search for: {} ***", search);
        Iterable<ExpertDto> expertDtos = UserMapper.INSTANCE.convertExpertIterator(expertService.findAll(search));
        log.info("*** : Search Results: {} ***", expertDtos);
        return expertDtos;
    }
}
