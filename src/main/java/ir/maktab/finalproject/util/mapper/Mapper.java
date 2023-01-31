package ir.maktab.finalproject.util.mapper;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    Customer convertCustomer(CustomerDto customerDto);

    @Mapping(source = "photoPath", target = "photo", qualifiedByName = "pathToBytes")
    Expert convertExpert(ExpertDto expertDto);

    @Named("pathToBytes")
    public static byte[] convertFileToBytes(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (NullPointerException | IOException e) {
            throw new PhotoValidationException("Photo Not Found");
        }
    }
}
