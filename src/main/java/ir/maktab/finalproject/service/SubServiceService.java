package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.repository.SubServiceRepository;
import ir.maktab.finalproject.service.exception.BaseServiceException;
import ir.maktab.finalproject.service.exception.SubServiceException;
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
public class SubServiceService {
    private final SubServiceRepository subServiceRepository;

    public SubService addSubService(SubService subService) {
        try {
            return subServiceRepository.save(subService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Base/Sub-Service Already Exists");
        }
    }

    public void deleteSubService(SubService subService) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subService.getSubName());
        if(foundSubService.isEmpty())
            throw new SubServiceException("Sub Service Not Found");

        subServiceRepository.delete(foundSubService.get());
    }

    public SubService editSubService(SubService subService) {
        Optional<SubService> foundSubService = subServiceRepository.findBySubName(subService.getSubName());
        if(foundSubService.isEmpty())
            throw new SubServiceException("Sub Service Not Found");
        foundSubService.get().setBasePrice(subService.getBasePrice());
        foundSubService.get().setDescription(subService.getDescription());
        return subServiceRepository.save(subService);
    }

    public List<SubService> findAllByBaseService(BaseService baseService) {
        return subServiceRepository.findAllByBaseService(baseService);
    }

    public Optional<SubService> findBySubName(String subName) {
        return subServiceRepository.findBySubName(subName);
    }
}
