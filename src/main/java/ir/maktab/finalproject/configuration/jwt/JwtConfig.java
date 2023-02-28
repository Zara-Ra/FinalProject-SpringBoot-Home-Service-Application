package ir.maktab.finalproject.configuration.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

//@ConfigurationProperties(prefix = "application.jwt")
@NoArgsConstructor
@Getter
@Setter
@Configuration
public class JwtConfig {
    @Value(value = "${application.jwt.secretKey}")
    private String secretKey;

    @Value(value = "${application.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value(value = "${application.jwt.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }
}