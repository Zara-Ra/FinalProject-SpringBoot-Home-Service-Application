package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerOrderDto {

    private Integer id;

    @Email
    private String customerEmail;

    @NotNull
    private String subServiceName;

    @NotNull
    private String price;

    private String description;

    @NotNull
    private String preferredDate;

    @NotNull
    private AddressDto address;
}
