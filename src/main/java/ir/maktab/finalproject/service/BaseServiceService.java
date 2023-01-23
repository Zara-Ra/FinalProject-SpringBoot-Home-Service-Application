package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.repository.BaseServiceRepository;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class BaseServiceService {
    private final BaseServiceRepository baseServiceRepository;

    public void addBaseService(BaseService baseService) {
        try {
            baseServiceRepository.save(baseService);
        } catch (PersistenceException e) {
            throw new UniqueViolationException("Base Service Already Exists");
        }
    }

    public void deleteBaseService(BaseService baseService) {
        baseServiceRepository.delete(baseService);
    }

    public List<BaseService> findAllBaseService() {
        return baseServiceRepository.findAll();
    }
}
