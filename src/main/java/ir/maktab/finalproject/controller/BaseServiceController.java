package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/base")
public class BaseServiceController extends MainController {

    private final BaseServiceService baseServiceService;

    public BaseServiceController(BaseServiceService baseServiceService) {
        this.baseServiceService = baseServiceService;
    }

    @PostMapping("/add")
    public String addBaseService(@Valid @RequestBody BaseServiceDto baseServiceDto) {
        baseServiceService.add(ServiceMapper.INSTANCE.convertBaseService(baseServiceDto));
        return "Base Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteBaseService(@PathVariable String name) {
        baseServiceService.delete(name);
        return "Base Service Deleted";
    }

    @GetMapping("/find/{name}")
    public BaseServiceDto findBaseService(@PathVariable String name) {
        return ServiceMapper.INSTANCE.convertBaseService(baseServiceService.findByName(name)
                .orElseThrow(() -> new BaseServiceException(messageSource.getMessage("errors.message.base_not_exists"))));
    }

    @GetMapping("/find-all")
    public List<BaseServiceDto> findAllBaseService() {
        return ServiceMapper.INSTANCE.convertBaseServiceList(baseServiceService.findAllBaseService());
    }
}
