package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.*;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.convert.Jsr310Converters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
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
    static byte[] convertPathToBytes(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (NullPointerException | IOException e) {
            throw new PhotoValidationException("Photo Not Found");
        }
    }

    List<ExpertDto> convertExpertList(List<Expert> allExpert);

    @Mapping(source = "customerEmail", target = "customer.email")
    @Mapping(source = "subServiceName", target = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd hh:mm")
    CustomerOrder convertOrder(CustomerOrderDto customerOrderDto);

    @Mapping(source = "customer.email" , target = "customerEmail")
    @Mapping(source = "subService.subName" , target = "subServiceName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd hh:mm")
    CustomerOrderDto convertOrder(CustomerOrder customerOrder);

    List<CustomerOrderDto> convertOrderList(List<CustomerOrder> orders);

    @Mapping(source = "expertEmail", target = "expert.email")
    @Mapping(source = "subServiceName", target = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd hh:mm")
    ExpertOffer convertOffer(ExpertOfferDto offerDto);


    @Mapping(target = "expertEmail", source = "expert.email")
    @Mapping(target = "subServiceName", source = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd hh:mm")
    ExpertOfferDto convertOffer(ExpertOffer offer);
}
