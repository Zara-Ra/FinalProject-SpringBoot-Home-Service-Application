package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertOfferDto {

    @NotNull
    private Integer id;

    @NotNull
    private Integer orderId;

    @Email
    @NotNull
    private String expertEmail;

    @NotNull
    private String subServiceName;

    @NotNull
    private double price;

    @NotNull
    private String preferredDate;

    @NotNull
    private String duration;
}
