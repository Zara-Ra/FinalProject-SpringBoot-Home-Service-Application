package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-14T19:32:24+0330",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public Customer convertCustomer(CustomerDto customerDto) {
        if ( customerDto == null ) {
            return null;
        }

        Customer.CustomerBuilder<?, ?> customer = Customer.builder();

        customer.id( customerDto.getId() );
        customer.email( customerDto.getEmail() );
        customer.password( customerDto.getPassword() );
        customer.firstName( customerDto.getFirstName() );
        customer.lastName( customerDto.getLastName() );

        return customer.build();
    }

    @Override
    public Expert convertExpert(ExpertDto expertDto) {
        if ( expertDto == null ) {
            return null;
        }

        Expert.ExpertBuilder<?, ?> expert = Expert.builder();

        expert.photo( UserMapper.convertPathToBytes( expertDto.getPhotoPath() ) );
        expert.id( expertDto.getId() );
        expert.email( expertDto.getEmail() );
        expert.password( expertDto.getPassword() );
        expert.firstName( expertDto.getFirstName() );
        expert.lastName( expertDto.getLastName() );

        return expert.build();
    }

    @Override
    public List<ExpertDto> convertExpertList(List<Expert> allExpert) {
        if ( allExpert == null ) {
            return null;
        }

        List<ExpertDto> list = new ArrayList<ExpertDto>( allExpert.size() );
        for ( Expert expert : allExpert ) {
            list.add( expertToExpertDto( expert ) );
        }

        return list;
    }

    @Override
    public Iterable<ExpertDto> convertExpertIterator(Iterable<Expert> all) {
        if ( all == null ) {
            return null;
        }

        ArrayList<ExpertDto> iterable = new ArrayList<ExpertDto>();
        for ( Expert expert : all ) {
            iterable.add( expertToExpertDto( expert ) );
        }

        return iterable;
    }

    @Override
    public Iterable<CustomerDto> convertCustomerIterator(Iterable<Customer> all) {
        if ( all == null ) {
            return null;
        }

        ArrayList<CustomerDto> iterable = new ArrayList<CustomerDto>();
        for ( Customer customer : all ) {
            iterable.add( customerToCustomerDto( customer ) );
        }

        return iterable;
    }

    protected ExpertDto expertToExpertDto(Expert expert) {
        if ( expert == null ) {
            return null;
        }

        ExpertDto expertDto = new ExpertDto();

        expertDto.setId( expert.getId() );
        expertDto.setEmail( expert.getEmail() );
        expertDto.setPassword( expert.getPassword() );
        expertDto.setFirstName( expert.getFirstName() );
        expertDto.setLastName( expert.getLastName() );

        return expertDto;
    }

    protected CustomerDto customerToCustomerDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setId( customer.getId() );
        customerDto.setEmail( customer.getEmail() );
        customerDto.setPassword( customer.getPassword() );
        customerDto.setFirstName( customer.getFirstName() );
        customerDto.setLastName( customer.getLastName() );

        return customerDto;
    }
}
