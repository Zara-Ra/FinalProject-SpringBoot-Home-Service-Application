package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.PhotoInfoDto;
import ir.maktab.finalproject.data.dto.ReviewDto;
import ir.maktab.finalproject.data.dto.response.ExpertResponseDto;
import ir.maktab.finalproject.data.dto.response.OrderResponseDto;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.mapper.OrderMapper;
import ir.maktab.finalproject.data.mapper.ReviewMapper;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.impl.ExpertService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String register(@Valid @ModelAttribute ExpertDto expertDto, HttpServletRequest request) {
        log.info("*** Add New Expert: {} ***", expertDto);
        Expert expert = expertService.register(UserMapper.INSTANCE.convertExpert(expertDto), getSiteURL(request));
        log.info("*** New Expert Added : {} ***", expert);
        return "Expert Registered Successfully, Check Your Email For Verification";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String code) {
        if (expertService.verify(code))
            return "Successfully Verified";
        else
            return "Verification Failed";
    }


    @PostMapping("/change-password")
    @PreAuthorize("hasRole('EXPERT')")
    public String changePassword(@Valid @RequestBody AccountDto accountDto, Principal principal) {
        log.info("*** Change Password for: {} ***", accountDto);
        accountDto.setEmail(principal.getName());
        Expert expert = expertService.changePassword(accountDto);
        log.info("*** Password Changed for: {} ***", expert);
        return "Password Changed For " + expert.getFirstName() + " " + expert.getLastName();
    }

    @GetMapping("/new-experts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExpertResponseDto> findNewExperts() {
        log.info("*** Find New Experts ***");
        List<ExpertResponseDto> expertResponseDtos = UserMapper.INSTANCE
                .convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.NEW));
        log.info("*** Found Experts With NEW Status: {} ***", expertResponseDtos);
        return expertResponseDtos;
    }

    @GetMapping("/unapproved-experts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExpertResponseDto> findUnApprovedExperts() {
        log.info("*** Find Unapproved Experts ***");
        List<ExpertResponseDto> expertResponseDtos = UserMapper.INSTANCE
                .convertExpertList(expertService.findAllExpertByStatus(ExpertStatus.WAITING_FOR_APPROVAL));
        log.info("*** Found Experts With WAITING_FOR_APPROVAL Status: {} ***", expertResponseDtos);
        return expertResponseDtos;
    }

    @GetMapping("/approve-expert")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExpert(@RequestParam @Min(1) Integer expertId) {
        log.info("*** Approve New Expert: {} ***", expertId);
        expertService.approveExpert(expertId);
        log.info("*** Expert Status Changed To APPROVED ***");
        return "Expert Status Changed To Approved";
    }

    @GetMapping("/assign-sub-service")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        log.info("*** Assign Sub Service: {},To Expert: {} ***", subServiceName, expertEmail);
        expertService.addSubServiceToExpert(subServiceName, expertEmail);
        log.info("*** Sub Service: {} Assigned To Expert: {} ***", subServiceName, expertEmail);
        return "Sub Service Assigned To Expert";
    }

    @GetMapping("/delete-sub-service")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSubService(@RequestParam String subServiceName, @RequestParam @Email String expertEmail) {
        log.info("*** Delete Sub Service: {},From Expert: {} ***", subServiceName, expertEmail);
        expertService.deleteSubServiceFromExpert(subServiceName, expertEmail);
        log.info("*** Sub Service: {} Deleted From Expert: {} ***", subServiceName, expertEmail);
        return "Sub Service Deleted From Expert";
    }

    @PostMapping("/save-photo")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT','CUSTOMER')")
    public String savePhoto(@Valid @RequestBody PhotoInfoDto photoInfoDto, Principal principal) {
        log.info("*** Save Expert Photo: {} ***", photoInfoDto);
        if(photoInfoDto.getOwnerEmail().isEmpty())
            photoInfoDto.setOwnerEmail(principal.getName());
        expertService.getExpertPhoto(photoInfoDto.getOwnerEmail(), photoInfoDto.getSavePath());
        log.info("*** Expert Photo Saved: {} ***", photoInfoDto);
        return "Photo Saved";
    }

    @GetMapping("/order-score")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ReviewDto orderScore(@RequestParam Integer orderId, Principal principal) {
        log.info("*** Show Score For Order: {} ***", orderId);
        String expertEmail = principal.getName();
        ReviewDto reviewDto = ReviewMapper.INSTANCE.convertReview(expertService.getOrderScore(orderId, expertEmail));
        log.info("*** Score For Expert {}, Order: {} is {} ***", expertEmail, orderId, reviewDto);
        return reviewDto;
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<ExpertResponseDto> search(@RequestParam String search) {
        log.info("*** Search for: {} ***", search);
        Iterable<ExpertResponseDto> expertDtos = UserMapper.INSTANCE.convertExpertIterator(expertService.findAll(search));
        log.info("*** : Search Results: {} ***", expertDtos);
        return expertDtos;
    }

    @GetMapping("/credit")
    @PreAuthorize("hasRole('EXPERT')")
    public double showCredit(Principal principal) {
        log.info("*** Show Credit For {} ***", principal.getName());
        double credit = expertService.getCredit(principal.getName());
        log.info("*** Credit For {}: {} ***", principal.getName(), credit);
        return credit;
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('EXPERT')")
    public List<OrderResponseDto> showAllOrders(Principal principal) {
        log.info("*** Find Order For Expert: {} ***", principal.getName());
        List<OrderResponseDto> orderDtoList = OrderMapper.INSTANCE.convertAcceptedOrderList(
                expertService.getAllOrders(principal.getName()));
        log.info("*** Orders For Expert: {}, {} ***", principal.getName(), orderDtoList);
        return orderDtoList;
    }

}
