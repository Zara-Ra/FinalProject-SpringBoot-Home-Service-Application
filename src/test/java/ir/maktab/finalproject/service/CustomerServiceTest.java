package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.impl.CustomerService;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    private static Customer customer;

    @BeforeAll
    static void beforeAll() {
        customer = Customer.builder()
                .email("customer@email.com")
                .password("customer")
                .firstName("Customer Name")
                .lastName("Customer Lastname")
                .customerOrderList(new ArrayList<>()).build();
    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("CustomerServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void signUpTest() {
        Customer newCustomer = customerService.signUp(customer);
        assertAll(
                () -> assertEquals(customer.getEmail(), newCustomer.getEmail()),
                () -> assertEquals(customer.getPassword(), newCustomer.getPassword()),
                () -> assertEquals(customer.getFirstName(), newCustomer.getFirstName()),
                () -> assertEquals(customer.getLastName(), newCustomer.getLastName())
        );
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "customer@email.com,12345678,Customer,Customer,Already Registered With This Email",
            "NIL,12345678,Customer,Customer,Invalid Email",
            "email.email.com,12345678,Customer,Customer,Invalid Email",
            "email@email.com,NIL,Customer,Customer,Invalid Password should be 8 characters including alphanumeric values",
            "email@email.com,123456,Customer,Customer,Invalid Password should be 8 characters including alphanumeric values",
            "email@email.com,12345678,NIL,Customer,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Customer123,Customer,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Customer,NIL,Invalid Name Only Alphabetic Characters Accepted",
            "email@email.com,12345678,Customer,Customer123,Invalid Name Only Alphabetic Characters Accepted"
    }, nullValues = "NIL")
    void invalidSignUpTest(String email, String password, String firstName, String lastName, String exceptionMsg) {
        Customer invalidCustomer = Customer.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName).build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> customerService.signUp(invalidCustomer));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Order(3)
    @Test
    void signInTest() {
        Customer signInCustomer = customerService.signIn("customer@email.com", "customer");
        assertEquals(customer, signInCustomer);
    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({"invalid@email.com,12345678,No User Registered With This Email",
            "customer@email.com,11111111,Incorrect Password"
    })
    void invalidSignInTest(String email, String password, String exceptionMsg) {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.signIn(email, password));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Order(5)
    @Test
    void changePasswordTest() {
        AccountDto accountDto = AccountDto.builder().email(customer.getEmail())
                .password("customer")
                .newPassword("12345678")
                .repeatPassword("12345678").build();
        Customer changePasswordCustomer = customerService.changePassword(accountDto);
        assertEquals("12345678", changePasswordCustomer.getPassword());
    }

    @Order(6)
    @Test
    void invalidChangePasswordTest() {
        AccountDto accountDto = AccountDto.builder().email(customer.getEmail())
                .password("invalidOldPassword")
                .newPassword("12345678")
                .repeatPassword("12345678").build();

        PasswordException exception = assertThrows(PasswordException.class,
                () -> customerService.changePassword(accountDto));
        assertEquals("Entered Password Doesn't Match", exception.getMessage());
    }

}
