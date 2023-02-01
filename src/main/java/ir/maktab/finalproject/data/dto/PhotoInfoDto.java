package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoInfoDto {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String ownerEmail;

    @NotNull
    private String savePath;
}
