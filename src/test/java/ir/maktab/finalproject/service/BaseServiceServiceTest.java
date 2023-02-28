package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.BaseService;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.impl.BaseServiceService;
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
        baseService = BaseService.builder().baseName("BaseService1").build();
    }

    @Order(1)
    @Test
    void addBaseServiceTest() {
        BaseService testBaseService = baseServiceService.add(baseService);
        assertEquals(baseService, testBaseService);
    }

    @Order(2)
    @Test
    void invalidAddBaseServiceViolateUniqueTest() {
        BaseService duplicateBaseService = BaseService.builder().baseName("BaseService1").build();
        UniqueViolationException exception = assertThrows(UniqueViolationException.class
                , () -> baseServiceService.add(duplicateBaseService));
        assertEquals("Base Service Already Exists", exception.getMessage());
    }

    @Order(4)
    @Test
    void findAllBaseServiceTest() {
        assertTrue(baseServiceService.findAllBaseService().stream().anyMatch(b -> b.equals(baseService)));
    }

    @Order(5)
    @Test
    void deleteBaseServiceTest() {
        baseServiceService.delete(baseService.getBaseName());
        assertTrue(baseServiceService.findByName(baseService.getBaseName()).isEmpty());
    }

    @Order(6)
    @Test
    void deleteUnavailableBaseServiceTest() {
        BaseService deleteBaseService = BaseService.builder().baseName("UnavailableBaseService").build();
        BaseServiceException exception = assertThrows(BaseServiceException.class, () -> baseServiceService.delete(deleteBaseService.getBaseName()));
        assertEquals("Base Service Not Exists", exception.getMessage());
    }
}
