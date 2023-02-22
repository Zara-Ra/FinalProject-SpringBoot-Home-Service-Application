package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.dto.AccountDto;
import ir.maktab.finalproject.data.entity.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface IRolesService<T> {
    T register(T user, String siteUrl);

    T changePassword(AccountDto accountDto);

    Optional<T> findByEmail(String email);

    Iterable<T> findAll(String searchCriteria);

    List<CustomerOrder> getAllOrders(String userEmail);
}
