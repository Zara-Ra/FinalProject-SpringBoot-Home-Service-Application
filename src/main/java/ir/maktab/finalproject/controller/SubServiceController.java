package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.SubServiceService;
import ir.maktab.finalproject.data.mapper.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub")
public class SubServiceController {

    private final SubServiceService subServiceService;

    public SubServiceController(SubServiceService subServiceService) {
        this.subServiceService = subServiceService;
    }

    @PostMapping("/add")
    public String addSubService(@RequestBody SubServiceDto subServiceDto){
        subServiceService.add(Mapper.INSTANCE.convertSubService(subServiceDto));
        return "Sub Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteSubService(@PathVariable String name){
        subServiceService.delete(name);
        return "Sub Service Deleted";
    }

    @GetMapping("/find/{name}")
    public SubServiceDto findSubService(@PathVariable String name){
        return Mapper.INSTANCE.convertSubService(subServiceService.findByName(name)
        .orElseThrow(()-> new SubServiceException("Sub Service Not Exits")));
    }

    @GetMapping("/find_all/{baseName}")
    public List<SubServiceDto> findAllSubServiceForBaseService(@PathVariable String baseName){
        return Mapper.INSTANCE.convertSubServiceList(subServiceService.findAllByBaseService(baseName));
    }

    @PutMapping("/edit")
    public String editSubService(@RequestBody SubServiceDto subServiceDto){
        subServiceService.editSubService(Mapper.INSTANCE.convertSubService(subServiceDto));
        return "Sub Service Edited";
    }
}
