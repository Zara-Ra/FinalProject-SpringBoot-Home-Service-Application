package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.AcceptedOrderDto;
import ir.maktab.finalproject.data.dto.AddressDto;
import ir.maktab.finalproject.data.dto.CustomerOrderDto;
import ir.maktab.finalproject.data.entity.Address;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.SubService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-12T16:08:51+0330",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public CustomerOrder convertOrder(CustomerOrderDto customerOrderDto) {
        if ( customerOrderDto == null ) {
            return null;
        }

        CustomerOrder.CustomerOrderBuilder customerOrder = CustomerOrder.builder();

        customerOrder.customer( customerOrderDtoToCustomer( customerOrderDto ) );
        customerOrder.subService( customerOrderDtoToSubService( customerOrderDto ) );
        try {
            if ( customerOrderDto.getPreferredDate() != null ) {
                customerOrder.preferredDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).parse( customerOrderDto.getPreferredDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        customerOrder.id( customerOrderDto.getId() );
        if ( customerOrderDto.getPrice() != null ) {
            customerOrder.price( Double.parseDouble( customerOrderDto.getPrice() ) );
        }
        customerOrder.description( customerOrderDto.getDescription() );
        customerOrder.address( addressDtoToAddress( customerOrderDto.getAddress() ) );

        return customerOrder.build();
    }

    @Override
    public CustomerOrderDto convertOrder(CustomerOrder customerOrder) {
        if ( customerOrder == null ) {
            return null;
        }

        CustomerOrderDto customerOrderDto = new CustomerOrderDto();

        customerOrderDto.setCustomerEmail( customerOrderCustomerEmail( customerOrder ) );
        customerOrderDto.setSubServiceName( customerOrderSubServiceSubName( customerOrder ) );
        if ( customerOrder.getPreferredDate() != null ) {
            customerOrderDto.setPreferredDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).format( customerOrder.getPreferredDate() ) );
        }
        customerOrderDto.setId( customerOrder.getId() );
        customerOrderDto.setPrice( String.valueOf( customerOrder.getPrice() ) );
        customerOrderDto.setDescription( customerOrder.getDescription() );
        customerOrderDto.setAddress( addressToAddressDto( customerOrder.getAddress() ) );

        return customerOrderDto;
    }

    @Override
    public List<CustomerOrderDto> convertOrderList(List<CustomerOrder> orders) {
        if ( orders == null ) {
            return null;
        }

        List<CustomerOrderDto> list = new ArrayList<CustomerOrderDto>( orders.size() );
        for ( CustomerOrder customerOrder : orders ) {
            list.add( convertOrder( customerOrder ) );
        }

        return list;
    }

    @Override
    public AcceptedOrderDto convertAcceptedOrder(CustomerOrder order) {
        if ( order == null ) {
            return null;
        }

        AcceptedOrderDto acceptedOrderDto = new AcceptedOrderDto();

        acceptedOrderDto.setCustomerEmail( customerOrderCustomerEmail( order ) );
        acceptedOrderDto.setSubServiceName( customerOrderSubServiceSubName( order ) );
        Double price = orderAcceptedExpertOfferPrice( order );
        if ( price != null ) {
            acceptedOrderDto.setOfferPrice( String.valueOf( price ) );
        }
        acceptedOrderDto.setOfferId( orderAcceptedExpertOfferId( order ) );
        if ( order.getStartDate() != null ) {
            acceptedOrderDto.setStartDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).format( order.getStartDate() ) );
        }
        if ( order.getFinishDate() != null ) {
            acceptedOrderDto.setFinishDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).format( order.getFinishDate() ) );
        }
        acceptedOrderDto.setId( order.getId() );
        acceptedOrderDto.setDescription( order.getDescription() );
        acceptedOrderDto.setStatus( order.getStatus() );
        acceptedOrderDto.setAddress( addressToAddressDto( order.getAddress() ) );

        return acceptedOrderDto;
    }

    protected Customer customerOrderDtoToCustomer(CustomerOrderDto customerOrderDto) {
        if ( customerOrderDto == null ) {
            return null;
        }

        Customer.CustomerBuilder<?, ?> customer = Customer.builder();

        customer.email( customerOrderDto.getCustomerEmail() );

        return customer.build();
    }

    protected SubService customerOrderDtoToSubService(CustomerOrderDto customerOrderDto) {
        if ( customerOrderDto == null ) {
            return null;
        }

        SubService.SubServiceBuilder subService = SubService.builder();

        subService.subName( customerOrderDto.getSubServiceName() );

        return subService.build();
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setCity( addressDto.getCity() );
        address.setStreet( addressDto.getStreet() );
        address.setNumber( addressDto.getNumber() );

        return address;
    }

    private String customerOrderCustomerEmail(CustomerOrder customerOrder) {
        if ( customerOrder == null ) {
            return null;
        }
        Customer customer = customerOrder.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String email = customer.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String customerOrderSubServiceSubName(CustomerOrder customerOrder) {
        if ( customerOrder == null ) {
            return null;
        }
        SubService subService = customerOrder.getSubService();
        if ( subService == null ) {
            return null;
        }
        String subName = subService.getSubName();
        if ( subName == null ) {
            return null;
        }
        return subName;
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setCity( address.getCity() );
        addressDto.setStreet( address.getStreet() );
        addressDto.setNumber( address.getNumber() );

        return addressDto;
    }

    private Double orderAcceptedExpertOfferPrice(CustomerOrder customerOrder) {
        if ( customerOrder == null ) {
            return null;
        }
        ExpertOffer acceptedExpertOffer = customerOrder.getAcceptedExpertOffer();
        if ( acceptedExpertOffer == null ) {
            return null;
        }
        double price = acceptedExpertOffer.getPrice();
        return price;
    }

    private Integer orderAcceptedExpertOfferId(CustomerOrder customerOrder) {
        if ( customerOrder == null ) {
            return null;
        }
        ExpertOffer acceptedExpertOffer = customerOrder.getAcceptedExpertOffer();
        if ( acceptedExpertOffer == null ) {
            return null;
        }
        Integer id = acceptedExpertOffer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
