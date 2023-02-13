package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditDto {

    @Email
    String customerEmail;

    double amount;
}
