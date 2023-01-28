package ir.maktab.finalproject.service.impl;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.repository.SubServiceRepository;
import ir.maktab.finalproject.service.ServiceService;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubServiceService implements ServiceService<SubService> {
    private final SubServiceRepository subServiceRepository;

    @Override
    public SubService add(SubService subService) {
        try {
            return subServiceRepository.save(subService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Base/Sub-Service Already Exists");
        }
    }

    @Override
    public void delete(SubService subService) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subService.getSubName());
        if (foundSubService.isEmpty())
            throw new SubServiceException("Sub Service Not Found");
        subServiceRepository.delete(foundSubService.get());
    }

    public SubService editSubService(SubService subService) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subService.getSubName());
        if (foundSubService.isEmpty())
            throw new SubServiceException("Sub Service Not Found");
        foundSubService.get().setBasePrice(subService.getBasePrice());
        foundSubService.get().setDescription(subService.getDescription());
        return subServiceRepository.save(subService);
    }

    public List<SubService> findAllByBaseService(BaseService baseService) {
        return subServiceRepository.findAllByBaseService(baseService);
    }

    @Override
    public Optional<SubService> findByName(String subName) {
        return subServiceRepository.findBySubName(subName);
    }


}
