package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

    @Pattern(regexp = "^[0-9]{16}$")
    private String cardNumber;

    @Pattern(regexp = "^([0-9]{4})-(0[1-9]|1[0-2])$")
    private String expirationDate;

    @Pattern(regexp = "^[0-9]{3,4}$")
    private String cvv2;

    private String captcha;
}
