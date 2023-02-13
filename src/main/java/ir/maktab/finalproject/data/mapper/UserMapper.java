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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
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

    Iterable<ExpertDto> convertExpertIterator(Iterable<Expert> all);
}
