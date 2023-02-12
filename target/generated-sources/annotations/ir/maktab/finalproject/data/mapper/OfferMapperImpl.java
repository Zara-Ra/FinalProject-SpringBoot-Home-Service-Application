package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.ExpertOfferDto;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-12T21:48:08+0330",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
public class OfferMapperImpl implements OfferMapper {

    @Override
    public ExpertOffer convertOffer(ExpertOfferDto offerDto) {
        if ( offerDto == null ) {
            return null;
        }

        ExpertOffer.ExpertOfferBuilder expertOffer = ExpertOffer.builder();

        expertOffer.expert( expertOfferDtoToExpert( offerDto ) );
        expertOffer.subService( expertOfferDtoToSubService( offerDto ) );
        expertOffer.customerOrder( expertOfferDtoToCustomerOrder( offerDto ) );
        try {
            if ( offerDto.getPreferredDate() != null ) {
                expertOffer.preferredDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).parse( offerDto.getPreferredDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        expertOffer.id( offerDto.getId() );
        expertOffer.price( offerDto.getPrice() );
        if ( offerDto.getDuration() != null ) {
            expertOffer.duration( Duration.parse( offerDto.getDuration() ) );
        }

        return expertOffer.build();
    }

    @Override
    public ExpertOfferDto convertOffer(ExpertOffer offer) {
        if ( offer == null ) {
            return null;
        }

        ExpertOfferDto expertOfferDto = new ExpertOfferDto();

        expertOfferDto.setExpertEmail( offerExpertEmail( offer ) );
        expertOfferDto.setSubServiceName( offerSubServiceSubName( offer ) );
        expertOfferDto.setOrderId( offerCustomerOrderId( offer ) );
        if ( offer.getPreferredDate() != null ) {
            expertOfferDto.setPreferredDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).format( offer.getPreferredDate() ) );
        }
        expertOfferDto.setId( offer.getId() );
        expertOfferDto.setPrice( offer.getPrice() );
        if ( offer.getDuration() != null ) {
            expertOfferDto.setDuration( offer.getDuration().toString() );
        }

        return expertOfferDto;
    }

    @Override
    public List<ExpertOfferDto> convertOfferList(List<ExpertOffer> offers) {
        if ( offers == null ) {
            return null;
        }

        List<ExpertOfferDto> list = new ArrayList<ExpertOfferDto>( offers.size() );
        for ( ExpertOffer expertOffer : offers ) {
            list.add( convertOffer( expertOffer ) );
        }

        return list;
    }

    protected Expert expertOfferDtoToExpert(ExpertOfferDto expertOfferDto) {
        if ( expertOfferDto == null ) {
            return null;
        }

        Expert.ExpertBuilder<?, ?> expert = Expert.builder();

        expert.email( expertOfferDto.getExpertEmail() );

        return expert.build();
    }

    protected SubService expertOfferDtoToSubService(ExpertOfferDto expertOfferDto) {
        if ( expertOfferDto == null ) {
            return null;
        }

        SubService.SubServiceBuilder subService = SubService.builder();

        subService.subName( expertOfferDto.getSubServiceName() );

        return subService.build();
    }

    protected CustomerOrder expertOfferDtoToCustomerOrder(ExpertOfferDto expertOfferDto) {
        if ( expertOfferDto == null ) {
            return null;
        }

        CustomerOrder.CustomerOrderBuilder customerOrder = CustomerOrder.builder();

        customerOrder.id( expertOfferDto.getOrderId() );

        return customerOrder.build();
    }

    private String offerExpertEmail(ExpertOffer expertOffer) {
        if ( expertOffer == null ) {
            return null;
        }
        Expert expert = expertOffer.getExpert();
        if ( expert == null ) {
            return null;
        }
        String email = expert.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String offerSubServiceSubName(ExpertOffer expertOffer) {
        if ( expertOffer == null ) {
            return null;
        }
        SubService subService = expertOffer.getSubService();
        if ( subService == null ) {
            return null;
        }
        String subName = subService.getSubName();
        if ( subName == null ) {
            return null;
        }
        return subName;
    }

    private Integer offerCustomerOrderId(ExpertOffer expertOffer) {
        if ( expertOffer == null ) {
            return null;
        }
        CustomerOrder customerOrder = expertOffer.getCustomerOrder();
        if ( customerOrder == null ) {
            return null;
        }
        Integer id = customerOrder.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
