package ir.maktab.finalproject.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class MessageSourceConfiguration {
    private final MessageSource messageSource;
    private final Locale locale = new Locale("en");

    public MessageSourceConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "", locale);
    }

    public String getMessage(String code, Object[] args, String defaultMsg, Locale locale) {
        return messageSource.getMessage(code, args, defaultMsg, locale);
    }
}
