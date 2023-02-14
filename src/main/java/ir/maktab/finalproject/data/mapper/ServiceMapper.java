package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    BaseService convertBaseService(BaseServiceDto baseServiceDto);

    BaseServiceDto convertBaseService(BaseService baseService);

    List<BaseServiceDto> convertBaseServiceList(List<BaseService> list);

    @Mapping(source = "baseServiceName", target = "baseService.baseName")
    SubService convertSubService(SubServiceDto subServiceDto);

    @Mapping(target = "baseServiceName", source = "baseService.baseName")
    SubServiceDto convertSubService(SubService subService);

    List<SubServiceDto> convertSubServiceList(List<SubService> list);

}
