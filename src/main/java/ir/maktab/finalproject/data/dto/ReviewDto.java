package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class ReviewDto {

    private Integer orderId;

    @Min(1)
    @Max(5)
    private int score;

    private String comment;
}
