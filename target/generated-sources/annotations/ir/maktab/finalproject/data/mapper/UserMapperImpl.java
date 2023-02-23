package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.dto.response.CustomerResponseDto;
import ir.maktab.finalproject.data.dto.response.ExpertResponseDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-23T16:13:04+0330",
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
    public Iterable<CustomerResponseDto> convertCustomerResponseIterator(Iterable<Customer> all) {
        if ( all == null ) {
            return null;
        }

        ArrayList<CustomerResponseDto> iterable = new ArrayList<CustomerResponseDto>();
        for ( Customer customer : all ) {
            iterable.add( customerToCustomerResponseDto( customer ) );
        }

        return iterable;
    }

    @Override
    public Expert convertExpert(ExpertDto expertDto) {
        if ( expertDto == null ) {
            return null;
        }

        Expert.ExpertBuilder<?, ?> expert = Expert.builder();

        expert.photo( UserMapper.map( expertDto.getPhoto() ) );
        expert.id( expertDto.getId() );
        expert.email( expertDto.getEmail() );
        expert.password( expertDto.getPassword() );
        expert.firstName( expertDto.getFirstName() );
        expert.lastName( expertDto.getLastName() );

        return expert.build();
    }

    @Override
    public List<ExpertResponseDto> convertExpertList(List<Expert> allExpert) {
        if ( allExpert == null ) {
            return null;
        }

        List<ExpertResponseDto> list = new ArrayList<ExpertResponseDto>( allExpert.size() );
        for ( Expert expert : allExpert ) {
            list.add( expertToExpertResponseDto( expert ) );
        }

        return list;
    }

    @Override
    public Iterable<ExpertResponseDto> convertExpertIterator(Iterable<Expert> all) {
        if ( all == null ) {
            return null;
        }

        ArrayList<ExpertResponseDto> iterable = new ArrayList<ExpertResponseDto>();
        for ( Expert expert : all ) {
            iterable.add( expertToExpertResponseDto( expert ) );
        }

        return iterable;
    }

    protected CustomerResponseDto customerToCustomerResponseDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();

        customerResponseDto.setId( customer.getId() );
        customerResponseDto.setEmail( customer.getEmail() );
        customerResponseDto.setFirstName( customer.getFirstName() );
        customerResponseDto.setLastName( customer.getLastName() );

        return customerResponseDto;
    }

    protected ExpertResponseDto expertToExpertResponseDto(Expert expert) {
        if ( expert == null ) {
            return null;
        }

        ExpertResponseDto expertResponseDto = new ExpertResponseDto();

        expertResponseDto.setId( expert.getId() );
        expertResponseDto.setEmail( expert.getEmail() );
        if ( expert.getStatus() != null ) {
            expertResponseDto.setStatus( expert.getStatus().name() );
        }
        expertResponseDto.setFirstName( expert.getFirstName() );
        expertResponseDto.setLastName( expert.getLastName() );
        expertResponseDto.setAverageScore( expert.getAverageScore() );

        return expertResponseDto;
    }
}
