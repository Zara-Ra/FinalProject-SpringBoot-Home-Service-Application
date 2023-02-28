package ir.maktab.finalproject.configuration.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsernameAndPasswordAuthenticationRequest {

    private String username;
    private String password;
}
