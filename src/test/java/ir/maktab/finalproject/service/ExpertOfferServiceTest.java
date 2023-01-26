package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ExpertOfferServiceTest {

    @Autowired
    private ExpertOfferService expertOfferService;

    private static SubService subService;
    private static Expert expert;
    private static CustomerOrder order;

    private static Date afterNow;

    private static Date beforeNow;
    private static Duration duration;

    @BeforeAll
    static void beforeAll() {

        long now = System.currentTimeMillis();
        afterNow = new Date(now + 900000);
        beforeNow = new Date(now - 900000);

        BaseService baseService = BaseService.builder().id(6).baseName("BaseService6").build();

        subService = SubService.builder()
                .id(6)
                .subName("SubService6")
                .basePrice(100)
                .baseService(baseService).build();
        List<SubService> subServiceList = List.of(subService);


        expert = Expert.builder()
                .id(6)
                .email("offer_service_test@email.com")
                .password("expert12")
                .firstName("Name")
                .lastName("Lastname")
                .subServiceList(subServiceList).build();

        Customer customer = Customer.builder()
                .id(6)
                .email("offer_service_test2@email.com")
                .password("customer")
                .firstName("Name")
                .lastName("Lastname")
                .customerOrderList(new ArrayList<>()).build();

        order = CustomerOrder.builder()
                .id(6)
                .customer(customer)
                .subService(subService)
                .price(200)
                .description("Order description")
                .status(OrderStatus.WAITING_FOR_EXPERT_OFFER)
                .preferredDate(afterNow).build();
        duration = Duration.ZERO.plusDays(1).plusHours(2).plusMinutes(30);

    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("OfferServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void submitOfferTest() {
        ExpertOffer offer = ExpertOffer.builder()
                .expert(expert)
                .subService(subService)
                .price(200)
                .customerOrder(order)
                //.isChosen(false)
                .duration(duration)
                .preferredDate(afterNow).build();

        ExpertOffer savedOffer = expertOfferService.submitOffer(offer);

        assertAll(
                () -> assertEquals(order,savedOffer.getCustomerOrder()),
                () -> assertEquals(expert,savedOffer.getExpert()),
                () -> assertEquals(subService,savedOffer.getSubService())
        );
    }
    /*@Test
    @Order(2)
    void invalidPriceForsubmitOfferTest() {
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(50)
                .description("Invalid price")
                .preferredDate(afterNow).build();
        OrderRequirementException exception = assertThrows(OrderRequirementException.class
                , () -> expertOfferService.requestOrder(customer, order));
        assertEquals("Price Of Order Should Be Greater Than Base Price Of The Sub-Service:( " +
                order.getSubService().getSubName() + " " + order.getSubService().getBasePrice() + " )", exception.getMessage());
    }

    @Order(3)
    @Test
    void invalidPreferredDateForsubmitOfferTest() {
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .subService(subService)
                .price(200)
                .description("Invalid preferred date")
                .preferredDate(beforeNow).build();
        OrderRequirementException exception = assertThrows(OrderRequirementException.class
                , () -> expertOfferService.requestOrder(customer, order));
        assertEquals("The Preferred Date Should Be After Now", exception.getMessage());
    }
*/

}
