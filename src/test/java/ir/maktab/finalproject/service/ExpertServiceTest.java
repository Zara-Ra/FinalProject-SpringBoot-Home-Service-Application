package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.mapper.UserMapper;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.impl.ExpertService;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ExpertServiceTest {
    @Autowired
    private ExpertService expertService;

    private static Expert expert;

    private static final String photoPath = "images/valid.jpg";

    private static SubService subService;

    @BeforeAll
    static void beforeAll() {
        expert = Expert.builder()
                .email("expert@email.com")
                .password("expert12")
                .firstName("Expert Name")
                .lastName("Expert Lastname")
                .subServiceList(new ArrayList<>()).build();

        subService = SubService.builder()
                .id(3)
                .subName("SubService3")
                .basePrice(100).build();
    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("ExpertServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void signUpTest() {
        byte[] photo = UserMapper.convertPathToBytes(photoPath);
        expert.setPhoto(photo);
        Expert newExpert = expertService.signUp(expert);
        assertAll(
                () -> assertEquals(expert.getEmail(), newExpert.getEmail()),
                () -> assertEquals(expert.getPassword(), newExpert.getPassword()),
                () -> assertEquals(expert.getFirstName(), newExpert.getFirstName()),
                () -> assertEquals(expert.getLastName(), newExpert.getLastName()),
                () -> assertEquals(expert.getPhoto(), newExpert.getPhoto())
        );
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "expert@email.com,12345678,Expert,Expert,images/valid.jpg,Already Registered With This Email",
            "NIL,12345678,Expert,Expert,images/valid.jpg,Invalid Email",
            "email.email.com,12345678,Expert,Expert,images/valid.jpg,Invalid Email",
            "email@email.com,NIL,Expert,Expert,images/valid.jpg,Invalid Password should be 8 characters including alphanumeric values",
            "email@email.com,123456,Expert,Expert,images/valid.jpg,Invalid Password should be 8 characters including alphanumeric values",
            "email@email.com,12345678,NIL,Expert,images/valid.jpg,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Expert123,Expert,images/valid.jpg,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Expert,NIL,images/valid.jpg,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Expert,Expert123,images/valid.jpg,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Expert,Expert,images/invalidSize.jpg,Photo Size Should Be Less Than 300 KiloBytes",
            "email@email.com,12345678,Expert,Expert,images/invalidType.gif,Invalid Photo Type Only 'jpeg' Accepted",
            "email@email.com,12345678,Expert,Expert,images/invalidMIME.jpg,Photo Not Found"
    }, nullValues = "NIL")
    void invalidSignUpTest(String email, String password, String firstName, String lastName, String path, String exceptionMsg) {
        byte[] photo = UserMapper.convertPathToBytes(photoPath);
        Expert invalidExpert = Expert.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .photo(photo).build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> expertService.signUp(invalidExpert));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Order(3)
    @Test
    void signInTest() {
        Expert signInExpert = expertService.signIn("expert@email.com", "expert12");
        assertEquals(expert, signInExpert);
    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({"invalid@email.com,expert12,No User Registered With This Email",
            "expert@email.com,11111111,Incorrect Password"
    })
    void invalidSignInTest(String email, String password, String exceptionMsg) {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> expertService.signIn(email, password));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Order(5)
    @Test
    void changePasswordTest() {
        AccountDto accountDto = AccountDto.builder().email(expert.getEmail())
                .password("expert12")
                .newPassword("12345678")
                .repeatPassword("12345678").build();

        Expert changePasswordExpert = expertService.changePassword(accountDto);
        assertEquals("12345678", changePasswordExpert.getPassword());
    }

    @Order(6)
    @Test
    void invalidChangePasswordTest() {
        AccountDto accountDto = AccountDto.builder().email(expert.getEmail())
                .password("invalidOldPassword")
                .newPassword("newPassword")
                .repeatPassword("newPassword").build();

        PasswordException exception = assertThrows(PasswordException.class,
                () -> expertService.changePassword(accountDto));
        assertEquals("Entered Password Doesn't Match", exception.getMessage());
    }

    @Order(7)
    @Test
    void findAllExpertByStatusTest() {
        List<Expert> allExpertByStatus = expertService.findAllExpertByStatus(ExpertStatus.NEW);
        assertEquals(1, allExpertByStatus.size());
    }

    @Order(8)
    @Test
    void setExpertStatusTest() {
        Expert changedExpert = expertService.setExpertStatus(expert.getId(), ExpertStatus.APPROVED);
        assertEquals(ExpertStatus.APPROVED, changedExpert.getStatus());
    }

    @Order(9)
    @Test
    void addSubServiceToExpertTest() {
        Expert changedExpert = expertService.addSubServiceToExpert(subService, expert);
        assertEquals(expert.getSubServiceList().size(), changedExpert.getSubServiceList().size());
    }

    @Order(10)
    @Test
    void invalidAddSubServiceToExpertWhenSubServiceInExpertExits() {
        SubServiceException exception = assertThrows(SubServiceException.class
                , () -> expertService.addSubServiceToExpert(subService, expert));
        assertEquals("Sub-Service Already Assigned To Expert", exception.getMessage());
    }

    @Order(11)
    @Test
    void invalidAddSubServiceToExpertWhenSubServiceUnavailable() {
        SubService invalidSubService = SubService.builder()
                .subName("Unavailable SubService")
                .basePrice(100).build();
        SubServiceException exception = assertThrows(SubServiceException.class
                , () -> expertService.addSubServiceToExpert(invalidSubService, expert));
        assertEquals("Sub-Service Unavailable", exception.getMessage());
    }

    @Order(12)
    @Test
    void deleteSubServiceFromExpertTest() {
        Expert changedExpert = expertService.deleteSubServiceFromExpert(subService, expert);
        assertEquals(0, changedExpert.getSubServiceList().size());
    }

    @Order(13)
    @Test
    void invalidDeleteSubServiceFromExpertWhenSubServiceInExpertNotExits() {
        SubServiceException exception = assertThrows(SubServiceException.class
                , () -> expertService.deleteSubServiceFromExpert(subService, expert));
        assertEquals("Expert Doesn't Have This Sub-Service", exception.getMessage());
    }

    @Order(14)
    @Test
    void getExpertPhotoTest() {
        String downloadPath = "images/download.jpg";
        expertService.getExpertPhoto("expert@email.com", downloadPath);
        File file = new File(downloadPath);
        assertTrue(file.exists());
    }
}
