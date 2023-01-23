package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        baseService = BaseService.builder().baseName("BaseService for SubService Test").build();
        subService = SubService.builder()
                .subName("SubServiceService Test")
                .baseService(baseService).build();
    }

    @Order(1)
    @Test
    void addSubServiceTest() {
        SubService testSubService = subServiceService.addSubService(subService);
        assertEquals(subService, testSubService);
    }

    @Order(2)
    @Test
    void invalidAddSubServiceViolateUniqueTest() {
        SubService duplicateSubService = SubService.builder().subName("SubServiceService Test").build();
        UniqueViolationException exception = assertThrows(UniqueViolationException.class
                , () -> subServiceService.addSubService(duplicateSubService));
        assertEquals("Base/Sub-Service Already Exists", exception.getMessage());
    }

    @Order(4)
    @Test
    void findAllByBaseServiceTest() {
        assertEquals(1, subServiceService.findAllByBaseService(baseService).size());
    }

    @Order(5)
    @Test
    void findBySubNameTest() {
        Optional<SubService> subServiceAvailable = subServiceService.findBySubName("SubServiceService Test");
        Optional<SubService> subServiceUnavailable = subServiceService.findBySubName("Test");

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

    @Order(8)
    @Test
    void deleteSubServiceTest() {
        subServiceService.deleteSubService(subService);
        assertEquals(0, subServiceService.findAllByBaseService(baseService).size());
    }

}
