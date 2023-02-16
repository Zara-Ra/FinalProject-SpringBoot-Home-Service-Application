package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpertDto {

    private Integer id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^([\\da-zA-Z]){8,}$")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String lastName;

    private String photoPath;
}
