package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.roles.Admin;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @Order(1)
    @Test
    void assignAdminTest() {
        Admin admin = adminService.assignAdmin();
        assertAll(
                () -> assertEquals(1, admin.getId()),
                () -> assertEquals("admin@admin.com", admin.getEmail()),
                () -> assertEquals("12345678", admin.getPassword())
        );
    }
}
