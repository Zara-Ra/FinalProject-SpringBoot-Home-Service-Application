package ir.maktab.finalproject.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDto {

    private Integer id;

    @Email
    private String email;

    @Pattern(regexp = "^([0-9a-zA-Z]){8,}$")
    private String password;

    @Pattern(regexp = "^([0-9a-zA-Z]){8,}$")
    private String newPassword;

    @Pattern(regexp = "^([0-9a-zA-Z]){8,}$")
    private String repeatPassword;
}
