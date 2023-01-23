package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.repository.SubServiceRepository;
import ir.maktab.finalproject.service.exception.UniqueViolationException;
import ir.maktab.finalproject.service.exception.UpdatableViolationException;
import jakarta.persistence.PersistenceException;
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
        } catch (PersistenceException e) {
            throw new UniqueViolationException("Base/Sub-Service Already Exists");
        }
    }

    public void deleteSubService(SubService subService) {
        subServiceRepository.delete(subService);
    }

    public void editSubService(SubService subService) {
        try {
            subServiceRepository.save(subService);
        } catch (DataIntegrityViolationException e) {
            throw new UpdatableViolationException("Can't Edit Sub-Service Name");
        }
    }

    public List<SubService> findAllByBaseService(BaseService baseService) {
        return subServiceRepository.findAllByBaseService(baseService);
    }

    public Optional<SubService> findBySubName(String subName) {
        return subServiceRepository.findBySubName(subName);
    }
}
