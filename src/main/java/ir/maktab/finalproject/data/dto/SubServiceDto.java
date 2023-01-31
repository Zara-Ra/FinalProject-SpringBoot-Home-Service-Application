package ir.maktab.finalproject.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubServiceDto {
    private Integer id;
    private String baseServiceName;
    private String subName;
    private double basePrice;
    private String description;
}
