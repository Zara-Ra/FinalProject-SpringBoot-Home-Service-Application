package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
import ir.maktab.finalproject.service.impl.SubServiceService;
import ir.maktab.finalproject.util.mapper.Mapper;
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

    @GetMapping("find_all/{name}")
    public List<SubServiceDto> findAllSubServiceForBaseService(@PathVariable String name){
        return Mapper.INSTANCE.convertSubServiceList(subServiceService.findAllByBaseService(name));
    }
}
