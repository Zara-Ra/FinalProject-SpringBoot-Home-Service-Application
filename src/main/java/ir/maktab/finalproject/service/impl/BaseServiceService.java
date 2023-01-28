package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.repository.BaseServiceRepository;
import ir.maktab.finalproject.service.ServiceService;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseServiceService implements ServiceService<BaseService> {
    private final BaseServiceRepository baseServiceRepository;

    @Override
    public BaseService add(BaseService baseService) {
        try {
            return baseServiceRepository.save(baseService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Base Service Already Exists");
        }
    }

    @Override
    public void delete(BaseService baseService) {
        Optional<BaseService> foundBaseService = baseServiceRepository.findByBaseName(baseService.getBaseName());
        if (foundBaseService.isEmpty())
            throw new BaseServiceException("Base Service Not Found");
        baseServiceRepository.delete(foundBaseService.get());
    }

    public List<BaseService> findAllBaseService() {
        return baseServiceRepository.findAll();
    }

    @Override
    public Optional<BaseService> findByName(String baseName) {
        return baseServiceRepository.findByBaseName(baseName);
    }
}