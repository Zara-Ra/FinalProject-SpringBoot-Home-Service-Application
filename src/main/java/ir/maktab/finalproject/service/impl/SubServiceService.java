package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.BaseService;
import ir.maktab.finalproject.data.entity.SubService;
import ir.maktab.finalproject.repository.SubServiceRepository;
import ir.maktab.finalproject.service.IService;
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubServiceService extends MainService implements IService<SubService> {

    private final SubServiceRepository subServiceRepository;
    private final BaseServiceService baseServiceService;

    public SubServiceService(SubServiceRepository subServiceRepository, BaseServiceService baseServiceService) {
        this.subServiceRepository = subServiceRepository;
        this.baseServiceService = baseServiceService;
    }

    @Override
    public SubService add(SubService subService) {
        BaseService baseService = baseServiceService.findByName(subService.getBaseService().getBaseName())
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.base_not_exists")));
        if (subServiceRepository.findBySubName(subService.getSubName()).isPresent())
            throw new UniqueViolationException(messageSource.getMessage("errors.message.duplicate_sub_service"));
        subService.setBaseService(baseService);
        return subServiceRepository.save(subService);
    }

    @Override
    public void delete(String subServiceName) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subServiceName);
        if (foundSubService.isEmpty())
            throw new SubServiceException(messageSource.getMessage("errors.message.sub_not_exists"));
        subServiceRepository.delete(foundSubService.get());
    }

    public SubService editSubService(SubService subService) {
        SubService foundSubService = subServiceRepository.findBySubName(subService.getSubName())
                .orElseThrow(() -> new SubServiceException(messageSource.getMessage("errors.message.sub_not_exists")));
        if (subService.getBasePrice() != 0)
            foundSubService.setBasePrice(subService.getBasePrice());
        if (subService.getDescription() != null && subService.getDescription().length() != 0)
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
