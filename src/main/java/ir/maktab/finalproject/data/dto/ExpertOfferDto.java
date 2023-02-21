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
public class ExpertOfferDto {

    private Integer id;

    @NotNull
    private Integer orderId;

    @Email
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
