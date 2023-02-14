package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.service.impl.CustomerOrderService;
import ir.maktab.finalproject.util.sort.SortExpertOffer;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class CustomerOrderServiceTest {

    @Autowired
    private CustomerOrderService customerOrderService;

    private static CustomerOrder order;

    private static CustomerOrder orderForSortTest;

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
                .description("description")
                .baseService(baseService).build();

        long now = System.currentTimeMillis();
        afterNow = new Date(now + 900000);
        beforeNow = new Date(now - 900000);


        orderForSortTest = CustomerOrder.builder()
                .id(5).build();
    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("OrderServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void requestOrderTest() {
        order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(200)
                .description("Order description")
                .preferredDate(afterNow).build();

        CustomerOrder savedOrder = customerOrderService.requestOrder(customer, order);

        assertAll(
                () -> assertEquals(1, customer.getCustomerOrderList().size()),
                () -> assertEquals(OrderStatus.WAITING_FOR_EXPERT_OFFER, customer.getCustomerOrderList().get(0).getStatus()),
                () -> assertEquals(order, savedOrder)
        );
    }

    @Test
    @Order(2)
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

    @Order(3)
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

    @Order(4)
    @Test
    void findAllBySubServiceAndStatusTest() {
        List<CustomerOrder> customerOrders = customerOrderService.findAllBySubServiceAndTwoStatus(subService.getSubName());
        assertTrue(customerOrders.contains(order));
    }

    @Order(5)
    @Test
    void getAllOffersTest() {
        List<ExpertOffer> defaultSortedOffers = customerOrderService.getAllOffersForOrder(orderForSortTest.getId(), SortExpertOffer.SortByPriceAcs);
        orderForSortTest.setExpertOfferList(defaultSortedOffers);
        assertEquals(200, defaultSortedOffers.get(0).getPrice());
    }

    @Order(6)
    @Test
    void getAllOffersForDcsTest() {
        List<ExpertOffer> sortedOffers = customerOrderService.getAllOffersForOrder(orderForSortTest.getId(), SortExpertOffer.SortByPriceDsc);
        assertEquals(300, sortedOffers.get(0).getPrice());
    }

}
