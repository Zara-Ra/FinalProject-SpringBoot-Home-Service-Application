package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerOrderDto {

    private Integer id;

    @NotNull
    @Email
    private String customerEmail;

    @NotNull
    private String subServiceName;
    @NotNull
    private String price;

    private String description;

    //@FutureOrPresent
    private String preferredDate;

    private AddressDto address;
}
