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
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.*;
import ir.maktab.finalproject.service.predicates.user.UserPredicateBuilder;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ExpertService extends MainService implements IRolesService<Expert> {

    private final ExpertRepository expertRepository;

    private final SubServiceService subServiceService;

    private final BCryptPasswordEncoder passwordEncoder;


    public ExpertService(ExpertRepository expertRepository, SubServiceService subServiceService, BCryptPasswordEncoder passwordEncoder) {
        this.expertRepository = expertRepository;
        this.subServiceService = subServiceService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Expert register(Expert expert) {
        validateNewExpert(expert);
        expert.setStatus(ExpertStatus.NEW);
        expert.setCredit(Credit.builder().amount(0).build());
        expert.setAverageScore(0);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        try {
            return expertRepository.save(expert);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(messageSource.getMessage("errors.message.duplicate_user"));
        }
    }

    @Override
    public Expert changePassword(AccountDto accountDto) {
        if (!accountDto.getNewPassword().equals(accountDto.getRepeatPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.password_mismatch"));

        Expert findExpert = expertRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));

        if(!passwordEncoder.matches(accountDto.getOldPassword(), findExpert.getPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.incorrect_old_password"));

        Validation.validatePassword(accountDto.getNewPassword());
        findExpert.setPassword(accountDto.getNewPassword());
        return expertRepository.save(findExpert);
    }

    public Optional<Expert> findByEmail(String email) {
        return expertRepository.findByEmail(email);
    }

    public List<Expert> findAllExpertByStatus(ExpertStatus status) {
        return expertRepository.findAllByStatus(status);
    }

    public Expert setExpertStatus(Integer expertId, ExpertStatus status) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.expert_not_exists")));
        expert.setStatus(status);
        return expertRepository.save(expert);
    }

    public Expert addSubServiceToExpert(String subServiceName, String expertEmail) {
        Expert expert = findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.expert_not_exists")));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.sub_not_exists")));

        if (!expert.getStatus().equals(ExpertStatus.APPROVED))
            throw new NotAllowedException(messageSource.getMessage("errors.message.expert_not_approved"));

        if (expert.getSubServiceList().stream().anyMatch(s -> s.equals(subService)))
            throw new SubServiceException(messageSource.getMessage("errors.message.invalid_sub_assign"));

        expert.getSubServiceList().add(subService);
        return expertRepository.save(expert);
    }

    public Expert deleteSubServiceFromExpert(String subServiceName, String expertEmail) {
        Expert expert = findByEmail(expertEmail)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.expert_not_exists")));
        SubService subService = subServiceService.findByName(subServiceName)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.sub_not_exists")));

        if (expert.getSubServiceList().stream().noneMatch(s -> s.equals(subService)))
            throw new SubServiceException(messageSource.getMessage("errors.message.invalid_sub_expert"));

        expert.getSubServiceList().remove(subService);
        return expertRepository.save(expert);
    }

    public void getExpertPhoto(String email, String photoPath) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));
        try (FileOutputStream fos = new FileOutputStream(photoPath)) {
            fos.write(expert.getPhoto());
        } catch (IOException e) {
            throw new PhotoValidationException(messageSource.getMessage("errors.message.photo_save_error"));
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

    public void updateExpert(Expert expert) {
        expertRepository.save(expert);
    }

    public void pay(Expert expert, double payAmount) {
        double expertCredit = expert.getCredit().getAmount() + 0.7 * payAmount;
        expert.getCredit().setAmount(expertCredit);
        expertRepository.save(expert);
    }

    public Review getOrderScore(Integer orderId, String expertEmail) {
        Expert expert = expertRepository.findByEmail(expertEmail)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));
        Review review = expert.getReviewList().stream()
                .filter(r -> r.getCustomerOrder().getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.no_review")));
        review.setComment("");
        return review;
    }

    public Iterable<Expert> findAll(String search) {
        if (search.isEmpty())
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_null_search"));

        UserPredicateBuilder builder = new UserPredicateBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        BooleanExpression expression = builder.build(Expert.class, "expert");
        return expertRepository.findAll(expression);
    }
}
