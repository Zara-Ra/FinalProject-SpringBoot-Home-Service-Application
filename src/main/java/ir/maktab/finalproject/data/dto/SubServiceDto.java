package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubServiceDto {

    private Integer id;

    @NotNull
    private String baseServiceName;

    @NotNull
    private String subName;

    private double basePrice;

    private String description;
}
