package ir.maktab.finalproject.controller;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
import ir.maktab.finalproject.service.impl.SubServiceService;
import ir.maktab.finalproject.util.mapper.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/base")
public class BaseServiceController {

    private final BaseServiceService baseServiceService;

    public BaseServiceController(BaseServiceService baseServiceService) {
        this.baseServiceService = baseServiceService;
    }

    @PostMapping("/add")
    public String addBaseService(@RequestBody BaseServiceDto baseServiceDto){
        baseServiceService.add(Mapper.INSTANCE.convertBaseService(baseServiceDto));
        return "Base Service Added";
    }

    @DeleteMapping("/delete/{name}")
    public String deleteBaseService(@PathVariable String name){
        baseServiceService.delete(name);
        return "Base Service Deleted";
    }

    @GetMapping("/find/{name}")
    public BaseServiceDto findBaseService(@PathVariable String name){
        return Mapper.INSTANCE.convertBaseService(baseServiceService.findByName(name)
        .orElseThrow(()-> new BaseServiceException("Base Service Not Exits")));
    }

    @GetMapping("/find_all")
    public List<BaseServiceDto> findAllBaseService(){
        return Mapper.INSTANCE.convertBaseServiceList(baseServiceService.findAllBaseService());
    }
}
