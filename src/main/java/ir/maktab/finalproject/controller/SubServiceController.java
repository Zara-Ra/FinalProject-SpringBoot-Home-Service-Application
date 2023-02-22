package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.SubService;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.SubServiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sub")
public class SubServiceController extends MainController {

    private final SubServiceService subServiceService;

    public SubServiceController(SubServiceService subServiceService) {
        this.subServiceService = subServiceService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        log.info("*** Add New Sub Service: {} ***", subServiceDto);
        SubService subService = subServiceService.add(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        log.info("*** Sub Service {} Added Successfully ***", subService);
        return "Sub Service Added";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSubService(@RequestParam String subServiceName) {
        log.info("*** Delete Sub Service: {} ***", subServiceName);
        subServiceService.delete(subServiceName);
        log.info("*** Sub Service {} Deleted Successfully ***", subServiceName);
        return "Sub Service Deleted";
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        log.info("*** Edit Sub Service: {} ***", subServiceDto);
        SubService subService = subServiceService.editSubService(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        log.info("*** Sub Service Edited: {} ***", subService);
        return "Sub Service Edited";
    }

    @GetMapping("/find")
    public SubServiceDto findSubService(@RequestParam String subServiceName) {
        log.info("*** Find Sub Service: {} ***", subServiceName);
        SubServiceDto subServiceDto = ServiceMapper.INSTANCE.convertSubService(subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new SubServiceException(messageSource.getMessage("errors.message.sub_not_exists"))));
        log.info("*** Found Sub Service: {} ***", subServiceDto);
        return subServiceDto;
    }

    @GetMapping("/find-all")
    public List<SubServiceDto> findAllSubServiceForBaseService(@RequestParam String baseServiceName) {
        log.info("*** Find All Sub Services For Base Service: {}  ***", baseServiceName);
        List<SubServiceDto> subServiceDtos = ServiceMapper.INSTANCE
                .convertSubServiceList(subServiceService.findAllByBaseService(baseServiceName));
        log.info("*** All Sub Services For Base Service {} : {} ***", baseServiceName, subServiceDtos);
        return subServiceDtos;

    }
}
