package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
    
    @Mapping(source = "expertEmail", target = "expert.email")
    @Mapping(source = "subServiceName", target = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd HH:mm")
    ExpertOffer convertOffer(ExpertOfferDto offerDto);

    @Mapping(target = "expertEmail", source = "expert.email")
    @Mapping(target = "subServiceName", source = "subService.subName")
    @Mapping(target = "preferredDate", source = "preferredDate", dateFormat = "yyyy-MM-dd HH:mm")
    ExpertOfferDto convertOffer(ExpertOffer offer);
}
