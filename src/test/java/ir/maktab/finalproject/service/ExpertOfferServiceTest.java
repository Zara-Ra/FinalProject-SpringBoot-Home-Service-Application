package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.data.entity.roles.Customer;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.OrderStatus;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.impl.ExpertOfferService;
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

    private static ExpertOffer offer;

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
                .preferredDate(afterNow)
                .expertOfferList(new ArrayList<>()).build();
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
        offer = ExpertOffer.builder()
                .expert(expert)
                .subService(subService)
                .price(200)
                .isChosen(false)
                .duration(duration)
                .preferredDate(afterNow).build();

        ExpertOffer savedOffer = expertOfferService.submitOffer(order, offer);

        assertAll(
                () -> assertEquals(expert, savedOffer.getExpert()),
                () -> assertEquals(subService, savedOffer.getSubService()),
                () -> assertEquals(OrderStatus.WAITING_FOR_EXPERT_SELECTION, order.getStatus())
        );
    }

    @Test
    @Order(2)
    void invalidPriceForSubmitOfferTest() {
        ExpertOffer offer = ExpertOffer.builder()
                .expert(expert)
                .subService(subService)
                .price(50)
                .duration(duration)
                .preferredDate(afterNow).build();
        OfferRequirementException exception = assertThrows(OfferRequirementException.class
                , () -> expertOfferService.submitOffer(order, offer));
        assertEquals("Price Of Offer Should Be Greater Than Base Price Of The Sub-Service ( " +
                offer.getSubService().getSubName() + " " + offer.getSubService().getBasePrice() + " )", exception.getMessage());
    }

    @Order(3)
    @Test
    void invalidPreferredDateForSubmitOfferTest() {
        ExpertOffer offer = ExpertOffer.builder()
                .expert(expert)
                .subService(subService)
                .price(200)
                .duration(duration)
                .preferredDate(beforeNow).build();
        OfferRequirementException exception = assertThrows(OfferRequirementException.class
                , () -> expertOfferService.submitOffer(order, offer));
        assertEquals("The Preferred Date Should Be After Now And After The Customers Preferred Date"
                , exception.getMessage());
    }

    @Order(4)
    @Test
    void invalidDurationForSubmitOfferTest() {
        ExpertOffer offer = ExpertOffer.builder()
                .expert(expert)
                .subService(subService)
                .price(200)
                .duration(null)
                .preferredDate(afterNow).build();
        OfferRequirementException exception = assertThrows(OfferRequirementException.class
                , () -> expertOfferService.submitOffer(order, offer));
        assertEquals("Duration Should Not Be Empty", exception.getMessage());
    }

    @Order(5)
    @Test
    void choseOfferTest() {
        expertOfferService.choseOffer(order, offer);
        long count = expertOfferService.countByIsChosen(true);
        assertEquals(1, count);
    }

    @Order(6)
    @Test
    void invalidChoseOfferTest() {
        CustomerOrder invalidOrder = CustomerOrder.builder()
                .expertOfferList(new ArrayList<>()).build();
        NotExistsException exception = assertThrows(NotExistsException.class
                , () -> expertOfferService.choseOffer(invalidOrder, offer));
        assertEquals("Offer Is Not For This Order", exception.getMessage());
    }
}
