package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private CustomerOrderService customerOrderService;

    private static SubService subService;
    private static Customer customer;

    private static Date afterNow;

    private static Date beforeNow;

    @BeforeAll
    static void beforeAll() {
        customer = Customer.builder()
                .id(5)
                .email("order_service_test@email.com")
                .password("customer")
                .firstName("Name")
                .lastName("Lastname")
                .customerOrderList(new ArrayList<>()).build();

        BaseService baseService = BaseService.builder().id(5).baseName("BaseService5").build();

        subService = SubService.builder()
                .id(5)
                .subName("SubService5")
                .basePrice(100)
                .baseService(baseService).build();

        long now = System.currentTimeMillis();
        afterNow = new Date(now + 900000);
        beforeNow = new Date(now - 900000);
    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("OrderServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(7)
    @Test
    void requestOrderTest() {
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(200)
                .description("Order description")
                .preferredDate(afterNow).build();

        customerOrderService.requestOrder(customer, order);

        assertAll(
                () -> assertEquals(1, customer.getCustomerOrderList().size()),
                () -> assertEquals(OrderStatus.WAITING_FOR_EXPERT_OFFER, customer.getCustomerOrderList().get(0).getStatus()));
    }

    @Test
    @Order(8)
    void invalidPriceForRequestOrderTest() {
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(50)
                .description("Invalid price")
                .preferredDate(afterNow).build();
        OrderRequirementException exception = assertThrows(OrderRequirementException.class
                , () -> customerOrderService.requestOrder(customer, order));
        assertEquals("Price Of Order Should Be Greater Than Base Price Of The Sub-Service:( " +
                order.getSubService().getSubName() + " " + order.getSubService().getBasePrice() + " )", exception.getMessage());
    }

    @Order(9)
    @Test
    void invalidPreferredDateForRequestOrderTest() {
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(200)
                .description("Invalid preferred date")
                .preferredDate(beforeNow).build();
        OrderRequirementException exception = assertThrows(OrderRequirementException.class
                , () -> customerOrderService.requestOrder(customer, order));
        assertEquals("The Preferred Date Should Be After Now", exception.getMessage());
    }
}
