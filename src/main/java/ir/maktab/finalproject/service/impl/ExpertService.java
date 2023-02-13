package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.Credit;
import ir.maktab.finalproject.data.entity.Review;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.entity.roles.enums.Role;
import ir.maktab.finalproject.data.entity.services.SubService;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.repository.ExpertRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.exception.*;
import ir.maktab.finalproject.service.predicates.UserPredicateBuilder;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ExpertService implements IRolesService<Expert> {
    private final ExpertRepository expertRepository;

    private final SubServiceService subServiceService;



    public ExpertService(ExpertRepository expertRepository, SubServiceService subServiceService) {
        this.expertRepository = expertRepository;
        this.subServiceService = subServiceService;
    }

    @Override
    public Expert signUp(Expert expert) {
        validateNewExpert(expert);
        expert.setStatus(ExpertStatus.NEW);
        expert.setCredit(Credit.builder().amount(0).build());
        expert.setAverageScore(0);
        expert.setRole(Role.ROLE_EXPERT);
        try {
            return expertRepository.save(expert);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Already Registered With This Email");
        }
    }

    @Override
    public Expert signIn(String email, String password) {
        validateAccount(email, password);
        Expert foundExpert = expertRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("No User Registered With This Email"));
        if (!foundExpert.getPassword().equals(password))
            throw new UserNotFoundException("Incorrect Password");
        return foundExpert;
    }

    @Override
    public Expert changePassword(AccountDto accountDto) {
        if (!accountDto.getNewPassword().equals(accountDto.getRepeatPassword()))
            throw new PasswordException("New Password And Repeat Password Don't Match");

        Expert findCustomer = expertRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No Username Registered With This Email"));

        if (!findCustomer.getPassword().equals(accountDto.getPassword()))
            throw new PasswordException("Incorrect Old Password");

        Validation.validatePassword(accountDto.getNewPassword());
        findCustomer.setPassword(accountDto.getNewPassword());
        return expertRepository.save(findCustomer);
    }

    public Optional<Expert> findByEmail(String email) {
        return expertRepository.findByEmail(email);
    }

    public List<Expert> findAllExpertByStatus(ExpertStatus status) {
        return expertRepository.findAllByStatus(status);
    }

    public Expert setExpertStatus(Integer expertId, ExpertStatus status) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new NotExistsException("Expert Not Exits"));
        expert.setStatus(status);
        return expertRepository.save(expert);
    }

    public Expert addSubServiceToExpert(String subServiceName, String expertEmail) {
        Expert expert = findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException("SubService Not Exits"));

        if (!expert.getStatus().equals(ExpertStatus.APPROVED))
            throw new NotAllowedException("Expert Is Not Approved Yet");

        if (expert.getSubServiceList().stream().anyMatch(s -> s.equals(subService)))
            throw new SubServiceException("Sub-Service Already Assigned To Expert");

        expert.getSubServiceList().add(subService);
        return expertRepository.save(expert);
    }

    public Expert deleteSubServiceFromExpert(String subServiceName, String expertEmail) {
        Expert expert = findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException("Expert Not Exits"));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException("SubService Not Exits"));

        if (expert.getSubServiceList().stream().noneMatch(s -> s.equals(subService)))
            throw new SubServiceException("Expert Doesn't Have This Sub-Service");

        expert.getSubServiceList().remove(subService);
        return expertRepository.save(expert);
    }

    public void getExpertPhoto(String email, String photoPath) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No Expert Registered With This Email"));
        try (FileOutputStream fos = new FileOutputStream(photoPath)) {
            fos.write(expert.getPhoto());
        } catch (IOException e) {
            throw new PhotoValidationException("Unable To Save Photo");
        }
    }

    private void validateNewExpert(Expert expert) {
        Validation.validateName(expert.getFirstName());
        Validation.validateName(expert.getLastName());
        validateAccount(expert.getEmail(), expert.getPassword());
        Validation.validatePhoto(expert.getPhoto());
    }

    private void validateAccount(String email, String password) {
        Validation.validateEmail(email);
        Validation.validatePassword(password);
    }

    public Expert updateExpert(Expert expert) {
        return expertRepository.save(expert);
    }

    public void pay(Expert expert, double payAmount) {
        double expertCredit = expert.getCredit().getAmount() + 0.7 * payAmount;
        expert.getCredit().setAmount(expertCredit);
        expertRepository.save(expert);
    }

    public Review getOrderScore(Integer orderId, String expertEmail) {
        Expert expert = expertRepository.findByEmail(expertEmail)
                .orElseThrow(() -> new UserNotFoundException("Expert Not Exists"));
        Review review = expert.getReviewList().stream()
                .filter(r -> r.getCustomerOrder().getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotExistsException("No Review For This Order"));
        review.setComment("");
        return review;
    }

    public Iterable<Expert> findAll(String search) {
        UserPredicateBuilder builder = new UserPredicateBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression expression = builder.build(Expert.class, "expert");
        return expertRepository.findAll(expression);
    }
}
