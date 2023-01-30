package ir.maktab.finalproject.util.mapper;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    BaseService convertBaseService(BaseServiceDto baseServiceDto);
    BaseServiceDto convertBaseService(BaseService baseService);

    List<BaseServiceDto> convertBaseServiceList(List<BaseService> list);
}
