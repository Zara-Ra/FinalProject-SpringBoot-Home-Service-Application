package ir.maktab.finalproject.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.*;
import ir.maktab.finalproject.data.entity.roles.Expert;
import ir.maktab.finalproject.data.enums.ExpertStatus;
import ir.maktab.finalproject.data.enums.Role;
import ir.maktab.finalproject.repository.ExpertRepository;
import ir.maktab.finalproject.service.IRolesService;
import ir.maktab.finalproject.service.MainService;
import ir.maktab.finalproject.service.exception.*;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.exception.ValidationException;
import ir.maktab.finalproject.util.search.predicates.user.UserPredicateBuilder;
import ir.maktab.finalproject.util.validation.Validation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpertService extends MainService implements IRolesService<Expert> {

    private final ExpertRepository expertRepository;

    private final SubServiceService subServiceService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String senderEmail;


    public ExpertService(ExpertRepository expertRepository, SubServiceService subServiceService, BCryptPasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.expertRepository = expertRepository;
        this.subServiceService = subServiceService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public Expert register(Expert expert, String siteURL) {
        validateNewExpert(expert);
        expert.setStatus(ExpertStatus.NEW);
        expert.setCredit(Credit.builder().amount(0).build());
        expert.setAverageScore(0);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));

        String randomCode = RandomStringUtils.random(64, true, true);
        expert.setVerificationCode(randomCode);
        expert.setEnabled(false);
        try {
            Expert saveExpert = expertRepository.save(expert);
            sendVerificationEmail(expert, siteURL);
            return saveExpert;
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(messageSource.getMessage("errors.message.duplicate_user"));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailVerificationException(messageSource.getMessage("errors.message.email_verification_error"));
        }
    }

    private void sendVerificationEmail(Expert expert, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = expert.getEmail();
        String fromAddress = senderEmail;
        String senderName = "Homser: Home Service Application";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Homser Application";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", expert.getFirstName() + " " + expert.getLastName());
        String verifyURL = siteURL + "/expert/verify?code=" + expert.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        Optional<Expert> user = expertRepository.findByVerificationCode(verificationCode);
        if (user.isEmpty() || user.get().isEnabled())
            return false;
        else {
            user.get().setVerificationCode(null);
            user.get().setEnabled(true);
            user.get().setStatus(ExpertStatus.WAITING_FOR_APPROVAL);
            expertRepository.save(user.get());
            return true;
        }
    }


    @Override
    public Expert changePassword(AccountDto accountDto) {
        if (!accountDto.getNewPassword().equals(accountDto.getRepeatPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.password_mismatch"));

        Expert findExpert = expertRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));

        if (!passwordEncoder.matches(accountDto.getOldPassword(), findExpert.getPassword()))
            throw new PasswordException(messageSource.getMessage("errors.message.incorrect_old_password"));

        Validation.validatePassword(accountDto.getNewPassword());
        findExpert.setPassword(passwordEncoder.encode(accountDto.getNewPassword()));
        return expertRepository.save(findExpert);
    }

    public Optional<Expert> findByEmail(String email) {
        return expertRepository.findByEmail(email);
    }

    public List<Expert> findAllExpertByStatus(ExpertStatus status) {
        return expertRepository.findAllByStatus(status);
    }

    public Expert approveExpert(Integer expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new NotExistsException(messageSource.getMessage("errors.message.expert_not_exists")));
        if (!expert.getStatus().equals(ExpertStatus.WAITING_FOR_APPROVAL))
            throw new NotAllowedException(messageSource.getMessage("errors.message.expert_email_not_verified"));
        expert.setStatus(ExpertStatus.APPROVED);
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
        review.setComment(null);
        return review;
    }

    public Iterable<Expert> findAll(String search) {
        if (search.isEmpty())
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_null_search"));
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        if (!search.equals("all")) {
            UserPredicateBuilder builder = new UserPredicateBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])([\\w-_@.]+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            expression = builder.build(Role.ROLE_EXPERT);
        }
        return expertRepository.findAll(expression);
    }

    public double getCredit(String email) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));
        return expert.getCredit().getAmount();
    }

    public List<CustomerOrder> getAllOrders(String expertEmail) {
        Expert expert = expertRepository.findByEmail(expertEmail)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("errors.message.expert_not_exists")));
        return expert.getAcceptedOfferList().stream().map(ExpertOffer::getCustomerOrder).collect(Collectors.toList());
    }
}
