package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.AcceptedOrderDto;
import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.roles.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customerEmail", target = "customer.email")
    @Mapping(source = "subServiceName", target = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd HH:mm")
    CustomerOrder convertOrder(CustomerOrderDto customerOrderDto);

    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "subService.subName", target = "subServiceName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd HH:mm")
    CustomerOrderDto convertOrder(CustomerOrder customerOrder);

    List<CustomerOrderDto> convertOrderList(List<CustomerOrder> orders);

    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "subService.subName", target = "subServiceName")
    @Mapping(source = "acceptedExpertOffer.expert.email", target = "expertEmail")//todo may not work
    @Mapping(source = "acceptedExpertOffer.price", target = "offerPrice")
    @Mapping(source = "acceptedExpertOffer.id", target = "offerId")
    @Mapping(target = "startDate", source = "startDate", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "finishDate", source = "finishDate", dateFormat = "yyyy-MM-dd HH:mm")
    AcceptedOrderDto convertAcceptedOrder(CustomerOrder customerOrder);

    Iterable<AcceptedOrderDto> convertCustomerOrderIterator(Iterable<CustomerOrder> all);
}
