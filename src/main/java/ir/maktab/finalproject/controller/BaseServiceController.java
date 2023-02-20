package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/base")
public class BaseServiceController extends MainController {

    private final BaseServiceService baseServiceService;

    public BaseServiceController(BaseServiceService baseServiceService) {
        this.baseServiceService = baseServiceService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBaseService(@Valid @RequestBody BaseServiceDto baseServiceDto) {
        log.info("*** Add New Base Service: {} ***", baseServiceDto);
        BaseService baseService = baseServiceService.add(ServiceMapper.INSTANCE.convertBaseService(baseServiceDto));
        log.info("*** Base Service {} Added Successfully ***", baseService);
        return "Base Service Added";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBaseService(@RequestParam String baseServiceName) {
        log.info("*** Delete Base Service: {} ***", baseServiceName);
        baseServiceService.delete(baseServiceName);
        log.info("*** Base Service {} Deleted Successfully ***", baseServiceName);
        return "Base Service Deleted";
    }

    @GetMapping("/find")
    public BaseServiceDto findBaseService(@RequestParam String baseServiceName) {
        log.info("*** Find Base Service: {} ***", baseServiceName);
        BaseServiceDto baseServiceDto = ServiceMapper.INSTANCE.convertBaseService(baseServiceService.findByName(baseServiceName)
                .orElseThrow(() -> new BaseServiceException(messageSource.getMessage("errors.message.base_not_exists"))));
        log.info("*** Found Base Service: {} ***", baseServiceDto);
        return baseServiceDto;
    }

    @GetMapping("/find-all")
    public List<BaseServiceDto> findAllBaseService() {
        log.info("*** Find All Base Services ***");
        List<BaseServiceDto> baseServiceDtos = ServiceMapper.INSTANCE.convertBaseServiceList(baseServiceService.findAllBaseService());
        log.info("*** All Base Services: {} ***", baseServiceDtos);
        return baseServiceDtos;
    }
}
