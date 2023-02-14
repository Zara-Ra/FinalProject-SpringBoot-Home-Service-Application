package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.mapper.ServiceMapper;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.SubServiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub")
public class SubServiceController extends MainController {

    private final SubServiceService subServiceService;

    public SubServiceController(SubServiceService subServiceService) {
        this.subServiceService = subServiceService;
    }

    @PostMapping("/add")
    public String addSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        subServiceService.add(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        return "Sub Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteSubService(@PathVariable String name) {
        subServiceService.delete(name);
        return "Sub Service Deleted";
    }

    @GetMapping("/find/{name}")
    public SubServiceDto findSubService(@PathVariable String name) {
        return ServiceMapper.INSTANCE.convertSubService(subServiceService.findByName(name)
                .orElseThrow(() -> new SubServiceException(messageSource.getMessage("errors.message.sub_not_exists"))));
    }

    @GetMapping("/find-all/{baseName}")
    public List<SubServiceDto> findAllSubServiceForBaseService(@PathVariable String baseName) {
        return ServiceMapper.INSTANCE.convertSubServiceList(subServiceService.findAllByBaseService(baseName));
    }

    @PutMapping("/edit")
    public String editSubService(@Valid @RequestBody SubServiceDto subServiceDto) {
        subServiceService.editSubService(ServiceMapper.INSTANCE.convertSubService(subServiceDto));
        return "Sub Service Edited";
    }
}
