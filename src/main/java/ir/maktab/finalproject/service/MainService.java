package ir.maktab.finalproject.service;

import ir.maktab.finalproject.configuration.MessageSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class MainService {
    @Autowired
    protected MessageSourceConfiguration messageSource;
}
