package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.repository.SubServiceRepository;
import ir.maktab.finalproject.service.IService;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubServiceService implements IService<SubService> {
    private final SubServiceRepository subServiceRepository;
    private final BaseServiceService baseServiceService;

    public SubServiceService(SubServiceRepository subServiceRepository, BaseServiceService baseServiceService) {
        this.subServiceRepository = subServiceRepository;
        this.baseServiceService = baseServiceService;
    }

    @Override
    public SubService add(SubService subService) {
        BaseService baseService = baseServiceService.findByName(subService.getBaseService().getBaseName())
                .orElseThrow(() -> new NotExitsException("Base Service Not Exists"));
        if (subServiceRepository.findBySubName(subService.getSubName()).isPresent())
            throw new UniqueViolationException("Sub-Service Already Exists");
        subService.setBaseService(baseService);
        return subServiceRepository.save(subService);
    }

    @Override
    public void delete(String subServiceName) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subServiceName);
        if (foundSubService.isEmpty())
            throw new SubServiceException("Sub Service Not Found");
        subServiceRepository.delete(foundSubService.get());
    }

    public SubService editSubService(SubService subService) {
        SubService foundSubService = subServiceRepository.findBySubName(subService.getSubName())
                .orElseThrow(() -> new SubServiceException("Sub Service Not Found"));
        foundSubService.setBasePrice(subService.getBasePrice());
        foundSubService.setDescription(subService.getDescription());
        return subServiceRepository.save(foundSubService);
    }

    public List<SubService> findAllByBaseService(String baseName) {
        return subServiceRepository.findAllByBaseService_BaseName(baseName);
    }

    @Override
    public Optional<SubService> findByName(String subName) {
        return subServiceRepository.findBySubName(subName);
    }

}
