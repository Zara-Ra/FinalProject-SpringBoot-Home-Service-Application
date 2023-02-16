package ir.maktab.finalproject.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private Integer id;

    @Email
    private String email;

    @Pattern(regexp = "^([\\da-zA-Z]){8,}$")
    private String password;

    @Pattern(regexp = "^([\\da-zA-Z]){8,}$")
    private String newPassword;

    @Pattern(regexp = "^([\\da-zA-Z]){8,}$")
    private String repeatPassword;
}
