package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseServiceDto {

    private Integer id;

    @NotNull
    private String baseName;
}
