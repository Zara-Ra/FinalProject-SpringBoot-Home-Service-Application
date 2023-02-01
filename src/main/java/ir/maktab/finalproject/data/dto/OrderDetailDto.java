package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDto {

    private String customerEmail;

    private String subServiceName;

    private String price;

    private String description;

    private String preferredDate;

    private AddressDto address;
}
