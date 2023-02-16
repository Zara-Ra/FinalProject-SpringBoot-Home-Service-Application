package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.SubServiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    public String addSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        log.info("*** Add New Sub Service: {} ***", subServiceDto);
        SubService subService = subServiceService.add(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        log.info("*** Sub Service {} Added Successfully ***",subService);
        return "Sub Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteSubService(@PathVariable String name) {
        log.info("*** Delete Sub Service: {} ***", name);
        subServiceService.delete(name);
        log.info("*** Sub Service {} Deleted Successfully ***",name);
        return "Sub Service Deleted";
    }

    @GetMapping("/find/{name}")
    public SubServiceDto findSubService(@PathVariable String name) {
        log.info("*** Find Sub Service: {} ***", name);
        SubServiceDto subServiceDto = ServiceMapper.INSTANCE.convertSubService(subServiceService.findByName(name)
                .orElseThrow(() -> new SubServiceException(messageSource.getMessage("errors.message.sub_not_exists"))));
        log.info("*** Found Sub Service: {} ***", subServiceDto);
        return subServiceDto;
    }

    @GetMapping("/find-all/{baseName}")
    public List<SubServiceDto> findAllSubServiceForBaseService(@PathVariable String baseName) {
        log.info("*** Find All Sub Services For Base Service: {}  ***",baseName);
        List<SubServiceDto> subServiceDtos = ServiceMapper.INSTANCE
                .convertSubServiceList(subServiceService.findAllByBaseService(baseName));
        log.info("*** All Sub Services For Base Service {} : {} ***",baseName,subServiceDtos);
        return subServiceDtos;

    }

    @PutMapping("/edit")
    public String editSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        log.info("*** Edit Sub Service: {} ***", subServiceDto);
        SubService subService = subServiceService.editSubService(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        log.info("*** Sub Service Edited: {} ***", subService);
        return "Sub Service Edited";
    }
}
