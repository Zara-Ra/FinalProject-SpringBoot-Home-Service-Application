package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.dto.AccountDto;

public interface IRolesService<T> {
    T register(T role);

    T changePassword(AccountDto accountDto);
}
