package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.repository.ExpertRepository;
import ir.maktab.finalproject.service.exception.PasswordException;
import ir.maktab.finalproject.service.exception.SubServiceException;
import ir.maktab.finalproject.service.exception.UserNotFoundException;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertService {
    private  final ExpertRepository expertRepository;
    private final SubServiceService subServiceService;

    public void signUp(Expert expert, String photoPath) {
        validateNewExpert(expert, photoPath);
        try {
            expert.setPhoto(convertFileToBytes(photoPath));
        } catch (IOException e) {
            throw new PhotoValidationException("Unable To Read Photo File");
        }
        expert.setStatus(ExpertStatus.NEW);
        expert.setCredit(Credit.builder().amount(0).build());
        expert.setAverageScore(0);
        expertRepository.save(expert);
    }

    public Expert signIn(String email, String password) {
        validateAccount(email, password);
        Expert foundExpert = expertRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("Not Registered With This Email"));
        if (!foundExpert.getPassword().equals(password))
            throw new UserNotFoundException("Incorrect Password");
        return foundExpert;
    }

    public void changePassword(Expert expert, String oldPassword, String newPassword) {
        if (!expert.getPassword().equals(oldPassword))
            throw new PasswordException("Entered Password Doesn't Match");
        Validation.validatePassword(newPassword);
        expert.setPassword(newPassword);
        expertRepository.save(expert);
    }

    public List<Expert> findAllExpertByStatus(ExpertStatus status) {
        return expertRepository.findAllByExpertStatus(status);
    }

    public void setExpertStatus(Expert expert, ExpertStatus status) {
        expert.setStatus(status);
        expertRepository.save(expert);
    }
    public void addSubServiceToExpert(SubService subService, Expert expert) {
        if (expert.getSubServiceList().stream().anyMatch(s -> s.equals(subService)))
            throw new SubServiceException("Sub-Service Already Assigned To Expert ");

        subServiceService.findBySubName(subService.getSubName())
                .orElseThrow(() -> new SubServiceException("Invalid Sub-Service"));

        expert.getSubServiceList().add(subService);
        expertRepository.save(expert);
    }

    public void deleteSubServiceFromExpert(SubService subService, Expert expert) {
        if (expert.getSubServiceList().stream().noneMatch(s -> s.equals(subService)))
            throw new SubServiceException("Expert Doesn't Have This Sub-Service ");
        expert.getSubServiceList().remove(subService);
        expertRepository.save(expert);
    }

    private void validateNewExpert(Expert expert, String photoPath) {
        Validation.validateName(expert.getFirstName());
        Validation.validateName(expert.getLastName());
        validateAccount(expert.getEmail(), expert.getPassword());
        //Validation.validatePhoto(expert.getPhoto());
        Validation.validatePhoto(photoPath);
    }

    private void validateAccount(String email, String password) {
        Validation.validateEmail(email);
        Validation.validatePassword(password);
    }

    private byte[] convertFileToBytes(String filePath) throws IOException {
        return Files.readAllBytes(Path.of(filePath));
    }
}