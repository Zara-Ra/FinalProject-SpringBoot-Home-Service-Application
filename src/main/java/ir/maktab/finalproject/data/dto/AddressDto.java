package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    private String city;

    private String street;

    private String number;
}
