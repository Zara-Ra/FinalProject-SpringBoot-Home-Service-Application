package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    public String addBaseService(@Valid @RequestBody BaseServiceDto baseServiceDto) {
        log.info("*** Add New Base Service: {} ***", baseServiceDto);
        BaseService baseService = baseServiceService.add(ServiceMapper.INSTANCE.convertBaseService(baseServiceDto));
        log.info("*** Base Service {} Added Successfully ***", baseService);
        return "Base Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteBaseService(@PathVariable String name) {
        log.info("*** Delete Base Service: {} ***", name);
        baseServiceService.delete(name);
        log.info("*** Base Service {} Deleted Successfully ***", name);
        return "Base Service Deleted";
    }

    @GetMapping("/find/{name}")
    public BaseServiceDto findBaseService(@PathVariable String name) {
        log.info("*** Find Base Service: {} ***", name);
        BaseServiceDto baseServiceDto = ServiceMapper.INSTANCE.convertBaseService(baseServiceService.findByName(name)
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
