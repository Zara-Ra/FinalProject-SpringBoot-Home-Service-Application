package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubServiceDto {

    private Integer id;

    @NotNull
    private String baseServiceName;

    @NotNull
    private String subName;

    private double basePrice;

    private String description;
}
