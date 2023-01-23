package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class BaseServiceServiceTest {
    @Autowired
    private BaseServiceService baseServiceService;

    private static BaseService baseService;

    @BeforeAll
    static void beforeAll() {
        baseService = BaseService.builder().baseName("BaseService Test").build();
    }

    @Order(1)
    @Test
    void addBaseServiceTest() {
        BaseService testBaseService = baseServiceService.addBaseService(baseService);
        assertEquals(baseService, testBaseService);
    }

    @Order(2)
    @Test
    void invalidAddBaseServiceViolateUniqueTest() {
        BaseService duplicateBaseService = BaseService.builder().baseName("BaseService Test").build();
        UniqueViolationException exception = assertThrows(UniqueViolationException.class
                , () -> baseServiceService.addBaseService(duplicateBaseService));
        assertEquals("Base Service Already Exists", exception.getMessage());
    }

    @Order(4)
    @Test
    void findAllBaseServiceTest() {
        assertTrue(baseServiceService.findAllBaseService().stream().anyMatch(b->b.equals(baseService)));
    }

    @Order(5)
    @Test
    void deleteBaseServiceTest() {
        baseServiceService.deleteBaseService(baseService);
        assertTrue(baseServiceService.findByBaseName(baseService.getBaseName()).isEmpty());
    }

   /* @Order(6)
    @Test
    void deleteUnavailableBaseServiceTest() {
        BaseService duplicateBaseService = BaseService.builder().baseName("BaseService Test").build();
        baseServiceService.deleteBaseService(duplicateBaseService);
        assertTrue(baseServiceService.findAllBaseService());
    }*/
}
