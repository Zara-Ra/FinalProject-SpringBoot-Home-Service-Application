package ir.maktab.finalproject.util.mapper;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    BaseService convertBaseService(BaseServiceDto baseServiceDto);
    BaseServiceDto convertBaseService(BaseService baseService);
    List<BaseServiceDto> convertBaseServiceList(List<BaseService> list);

    @Mapping(source = "baseServiceName", target = "baseService.baseName")
    SubService convertSubService(SubServiceDto subServiceDto);

    @Mapping(target = "baseServiceName", source = "baseService.baseName")
    SubServiceDto convertSubService(SubService subService);
    List<SubServiceDto> convertSubServiceList(List<SubService> list);
}
