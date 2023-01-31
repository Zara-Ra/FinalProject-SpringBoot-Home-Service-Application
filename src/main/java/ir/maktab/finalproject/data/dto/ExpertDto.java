package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertDto {

    private Integer id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^([0-9a-zA-Z]){8,}$")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String lastName;

    private String photoPath;
}
