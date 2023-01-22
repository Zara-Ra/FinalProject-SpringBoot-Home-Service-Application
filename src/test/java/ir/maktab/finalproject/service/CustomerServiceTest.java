package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.roles.Admin;
import ir.maktab.finalproject.data.entity.roles.Customer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .lastName("Customer Lastname").build();
    }

    @Order(1)
    @Test
    void validSignUpTest(){
        customerService.signUp(customer);
    }
}
