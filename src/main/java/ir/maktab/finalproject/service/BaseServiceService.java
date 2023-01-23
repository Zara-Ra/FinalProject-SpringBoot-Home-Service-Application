package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.repository.BaseServiceRepository;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BaseServiceService {
    private final BaseServiceRepository baseServiceRepository;

    public BaseService addBaseService(BaseService baseService) {
        try {
            return baseServiceRepository.save(baseService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Base Service Already Exists");
        }
    }

    public void deleteBaseService(BaseService baseService) {
        Optional<BaseService> foundBaseService = baseServiceRepository.findByBaseName(baseService.getBaseName());
        if(foundBaseService.isEmpty())
            throw new BaseServiceException("Base Service Not Found");
        baseServiceRepository.delete(foundBaseService.get());
    }

    public List<BaseService> findAllBaseService() {
        return baseServiceRepository.findAll();
    }

    public Optional<BaseService> findByBaseName(String baseName){ return baseServiceRepository.findByBaseName(baseName);}
}
