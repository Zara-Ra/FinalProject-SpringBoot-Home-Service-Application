package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

    @NotNull
    private String preferredDate;

    @NotNull
    private AddressDto address;
}
