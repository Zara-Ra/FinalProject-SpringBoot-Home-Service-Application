package ir.maktab.finalproject.service;

import ir.maktab.finalproject.data.dto.AccountDto;

public interface IRolesService<T> {
    T signUp(T role);

    T signIn(String email, String password);

    T changePassword(AccountDto accountDto);
}
