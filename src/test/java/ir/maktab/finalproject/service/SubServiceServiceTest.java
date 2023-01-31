package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.impl.SubServiceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class SubServiceServiceTest {
    @Autowired
    private SubServiceService subServiceService;

    private static SubService subService;
    private static BaseService baseService;

    @BeforeAll
    static void beforeAll() {
        baseService = BaseService.builder().id(4).baseName("BaseService4").build();
        subService = SubService.builder()
                .subName("SubService4")
                .baseService(baseService).build();
    }

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("SubServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void addSubServiceTest() {
        SubService testSubService = subServiceService.add(subService);
        assertEquals(subService, testSubService);
    }

    @Order(2)
    @Test
    void invalidAddSubServiceViolateUniqueTest() {
        SubService duplicateSubService = SubService.builder().subName("SubService4").build();
        UniqueViolationException exception = assertThrows(UniqueViolationException.class
                , () -> subServiceService.add(duplicateSubService));
        assertEquals("Base/Sub-Service Already Exists", exception.getMessage());
    }

    @Order(4)
    @Test
    void findAllByBaseServiceTest() {
        assertEquals(1, subServiceService.findAllByBaseService(baseService.getBaseName()).size());
    }

    @Order(5)
    @Test
    void findBySubNameTest() {
        Optional<SubService> subServiceAvailable = subServiceService.findByName("SubService4");
        Optional<SubService> subServiceUnavailable = subServiceService.findByName("Test");

        assertAll(
                () -> assertTrue(subServiceAvailable.isPresent()),
                () -> assertTrue(subServiceUnavailable.isEmpty())
        );
    }

    @Order(6)
    @Test
    void editSubServiceTest() {
        subService.setBasePrice(400);
        subService.setDescription("New Description");
        SubService editSubService = subServiceService.editSubService(subService);
        assertAll(
                () -> assertEquals(400, editSubService.getBasePrice()),
                () -> assertEquals("New Description", editSubService.getDescription())
        );
    }

    @Order(7)
    @Test
    void invalidEditSubServiceTest() {
        SubService unavailableSubService = SubService.builder().subName("Unavailable").build();
        assertThrows(SubServiceException.class, () -> subServiceService.editSubService(unavailableSubService));
    }

    @Order(8)
    @Test
    void deleteSubServiceTest() {
        subServiceService.delete(subService.getSubName());
        assertEquals(0, subServiceService.findAllByBaseService(baseService.getBaseName()).size());
    }

    @Order(9)
    @Test
    void invalidDeleteSubServiceTest() {
        SubService unavailableSubService = SubService.builder().subName("Unavailable").build();
        SubServiceException exception = assertThrows(SubServiceException.class, () -> subServiceService.editSubService(unavailableSubService));
        assertEquals("Sub Service Not Found", exception.getMessage());
    }

}
