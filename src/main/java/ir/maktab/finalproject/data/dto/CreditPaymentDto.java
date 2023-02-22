package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.maktab.finalproject.data.enums.PaymentType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditPaymentDto {

    private String customerEmail;

    @Min(1)
    private Integer orderId;

    private PaymentType paymentType;
}

