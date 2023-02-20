package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.CustomerDto;
import ir.maktab.finalproject.data.dto.ExpertDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Customer convertCustomer(CustomerDto customerDto);

    @Mapping(source = "photo", target = "photo", qualifiedByName = "FileToBytes")
    Expert convertExpert(ExpertDto expertDto);

    @Named("FileToBytes")
    static byte[] map(MultipartFile filePath) {
        try {
            return filePath.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Mapping(source = "photo", target = "photo", qualifiedByName = "ByteToNull")
    ExpertDto convertExpert(Expert expert);

    @Named("ByteToNull")
    static MultipartFile map(byte[] file){
        return null;
    }

    List<ExpertDto> convertExpertList(List<Expert> allExpert);

    Iterable<ExpertDto> convertExpertIterator(Iterable<Expert> all);

    Iterable<CustomerDto> convertCustomerIterator(Iterable<Customer> all);
}
