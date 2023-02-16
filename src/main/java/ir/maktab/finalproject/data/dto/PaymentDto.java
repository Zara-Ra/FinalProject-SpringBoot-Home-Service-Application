package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {

    private Integer orderId;

    @Pattern(regexp = "^\\d{16}$")
    private String cardNumber;

    @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])$")
    private String expirationDate;

    @Pattern(regexp = "^\\d{3,4}$")
    private String cvv2;

    private String captcha;
}
