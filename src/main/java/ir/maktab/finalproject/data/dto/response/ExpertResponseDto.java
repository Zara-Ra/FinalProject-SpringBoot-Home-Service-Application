package ir.maktab.finalproject.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpertResponseDto {

    private Integer id;

    @Email
    private String email;

    private String status;

    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String lastName;

    private double averageScore;

}
